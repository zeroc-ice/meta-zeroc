ICE_VERSION = "3.7.0"

require zeroc-ice.inc

inherit bluetooth

BUILD_ICE_BT = "${@base_contains("DISTRO_FEATURES", "bluetooth", "yes", "", d)}"
BLUEZ_DEPS = "bluez4 dbus-glib expat"

DEPENDS_append_class-target += "${@base_contains("DISTRO_FEATURES", "bluetooth", "${BLUEZ_DEPS}", "", d)}"

SRC_URI = "git://github.com/zeroc-ice/icee.git;protocol=http;branch=master"
SRCREV = "73cebce80b860d0afce5a11fad4715a9a76db608"

EXTRA_OEMAKE += "BUILD_ICE_BT=${BUILD_ICE_BT}"
