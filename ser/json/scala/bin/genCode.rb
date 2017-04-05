#!/bin/env ruby

require 'dataMetaDom' # summon the DataMeta Core which includes DataMetaDOM parser and more.
# see the gem docs here: https://github.com/eBayDataMeta/DataMeta-gems/tree/master/meta/core/dom
require 'dataMetaDom/pojo' # summon the POJO+Comparators generator which is the part of the dataMetaDom gem described above
require 'dataMetaDom/util' # summon the DataMetaDOM utilities modules, constants and methods
require 'dataMetaJacksonSer' # JSON Serialization
require 'dataMetaJacksonSer/util'  # JSON Serialization utility module

require 'fileutils' # We'll need FileUtils too
# see docs here: https://ruby-doc.org/stdlib-2.2.2/libdoc/fileutils/rdoc/FileUtils.html


# Include the key modules to avoid long module prefixes, to make the code more readable
include DataMetaDom, DataMetaDom::PojoLexer

# Target to gen the Java files to, minus package
JAVA_TARGET = File.join(%w{src main java})
SCALA_TARGET = File.join(%w{src main scala})

dataModel = Model.new

begin
    # Parse the model from the definition in ./model/logging/dmDom
    dataModel.parse(File.join(%w{model jacksonSer.dmDom}), options={autoVerNs: true})
rescue Exception => x
    # handle any errors here, notably output the DataMetaDOM model's diagnostics:
    $stderr.puts "ERROR #{x.message}; #{dataModel.diagn}"
    $stderr.puts x.backtrace.join("\n\t") # show regular stack trace
    exit 1 # and exit with error code
end

recs = dataModel.records
firstRecord = recs.values.first

NAMESPACE = firstRecord.namespace

javaPackage, base, packagePath = assertNamespace(firstRecord.name)

GEN_TARGET = File.join(JAVA_TARGET, packagePath)

# Clean all Java files to prevent any old stuff from lingering around
# see the docs for the rm_r method here: https://ruby-doc.org/stdlib-2.2.2/libdoc/fileutils/rdoc/FileUtils.html#method-c-rm_r
FileUtils.rm_r(GEN_TARGET, force: true, verbose: false, noop: false)

genPojos(dataModel, JAVA_TARGET) # generate the POJOs

DataMetaJacksonSer::genJacksonables(dataModel, SCALA_TARGET) # generate the Jacksonables

# Generate full comparators
DataMetaDom::PojoLexer.genDataMetaSames dataModel, JAVA_TARGET # style = FULL_COMPARE -- this is a default

puts "POJOs and DmSames generated into #{JAVA_TARGET}"
puts "Jackonables generated into #{SCALA_TARGET}"

puts 'Done.'

