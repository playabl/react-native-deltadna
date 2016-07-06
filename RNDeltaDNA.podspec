require 'json'
version = JSON.parse(File.read('package.json'))["version"]

Pod::Spec.new do |s|

  s.name           = "RNDeltaDNA"
  s.version        = version
  s.summary        = "Integrating DeltaDNA Game Analytics into your React Native app."
  s.homepage       = "https://github.com/playabl/react-native-deltadna"
  s.license        = "MIT"
  s.author         = { "Johannes Stein" => "johannes@playabl.com" }
  s.platform       = :ios, "7.0"
  s.source         = { :git => "https://github.com/playabl/react-native-deltadna.git", :tag => "v#{s.version}" }
  s.source_files   = 'ios/*.{h,m}'
  s.preserve_paths = "**/*.js"
  s.dependency 'React'
  s.dependency 'DeltaDNA'

end
