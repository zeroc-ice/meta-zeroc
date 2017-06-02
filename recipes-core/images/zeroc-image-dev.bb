require zeroc-image-testing.bb

DESCRIPTION = "Everything required to build ZeroC Ice from source and run the test suite."

IMAGE_FEATURES += "dev-pkgs staticdev-pkgs tools-sdk tools-debug tools-profile"

IMAGE_INSTALL += " mcpp"
