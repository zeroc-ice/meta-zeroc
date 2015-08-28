require zeroc-image-testing.bb

DESCRIPTION = "Everything required to build ZeroC Ice from source and run the test suite."

IMAGE_FEATURES += "dev-pkgs tools-sdk tools-debug tools-profile"
