require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-mapbox"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  react-native-mapbox
                   DESC
  s.homepage     = "https://github.com/aliustaoglu/react-native-mapbox"
  s.license      = "MIT"
  # s.license    = { :type => "MIT", :file => "FILE_LICENSE" }
  s.authors      = { "Cuneyt Aliustaoglu" => "aliustaoglu@yahoo.com" }
  s.platforms    = { :ios => "9.0", :tvos => "10.0" }
  s.source       = { :git => "https://github.com/aliustaoglu/react-native-mapbox.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,m,swift}"
  s.requires_arc = true
  #s.resources = "ios/MapAssets.xcassets"
  # To store assets such as pin.png etc.
  s.resource_bundles = {'MapAssets' => ['ios/MapAssets.xcassets']}

  s.dependency "React"
  s.dependency 'Mapbox-iOS-SDK', '~> 5.5'
  #s.dependency 'AFNetworking', '~> 3.0'
  # s.dependency "..."
end

