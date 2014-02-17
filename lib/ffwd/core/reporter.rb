module FFWD
  class Core; end

  class Core::Reporter
    def initialize reporters
      @reporters = reporters
    end

    def report!
      @reporters.each do |reporter|
        reporter.report! do |d|
          yield d
        end
      end
    end
  end
end