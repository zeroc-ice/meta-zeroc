SUMMARY  = "ZeroC Ice for Yocto"
DESCRIPTION = "Comprehensive RPC framework"
HOMEPAGE = "https://zeroc.com"
SECTION  = "libs"

LICENSE  = "GPLv2"
LIC_FILES_CHKSUM = "file://ICE_LICENSE;md5=51cbbfae4849a92975efff73e4de3a0c \
                    file://LICENSE;md5=1b65bb9598f16820aab2ae1dd2a51f9f"

SRCREV  = "${AUTOREV}"

PV = "3.7.3+git${SRCPV}"
PR = "r0"

SRC_URI = "git://github.com/zeroc-ice/ice;branch=3.7"

S = "${WORKDIR}/git"
B = "${WORKDIR}/git"

inherit bluetooth pkgconfig python3native python3-dir

DEPENDS  = "openssl bzip2 mcpp lmdb expat python3"
DEPENDS_append_class-target = " ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', d.expand('${BLUEZ} dbus-glib'), '', d)}"

DEPENDS_append_class-target = " zeroc-ice-native"
RDEPENDS_${PN} = "openssl bzip2 expat"

#
# OECORE_SDK_VERSION is always set in an SDK. To get the Ice build system to
# detect a Yocto/OE build just need to to be set here.
#
EXTRA_OEMAKE = "OECORE_SDK_VERSION=yes V=1"

EXTRA_OEMAKE_append_class-target = " CONFIGS=all ICE_HOME=${STAGING_DIR_NATIVE}/usr ICE_BIN_DIST=compilers"

do_compile_class-target () {
    oe_runmake LANGUAGES="cpp python" srcs
}

do_configure_class-target () {
    oe_runmake LANGUAGES="cpp python" distclean
}

do_install_class-target () {
    oe_runmake LANGUAGES="cpp python" \
	DESTDIR=${D} prefix=${prefix} USR_DIR_INSTALL=yes \
	PYTHON_INSTALLDIR=${PYTHON_SITEPACKAGES_DIR} install
}

#
# Use bellow targets for native and naivesdk
#
do_compile () {
    oe_runmake -C cpp slice2cpp slice2py
}

do_configure () {
    oe_runmake distclean -C cpp
}

do_install () {
    oe_runmake DESTDIR=${D} prefix=${prefix} USR_DIR_INSTALL=yes -C cpp slice2cpp_install slice2py_install
}

FILES_${PN} += "${base_prefix}/usr/share/ice/templates.xml \
                ${base_prefix}/usr/share/ice/ICE_LICENSE \
                ${base_prefix}/usr/share/ice/LICENSE"

# Add slice compilers and -slice dependency to -dev
FILES_${PN}-dev += "${bindir}/slice2*"
DEPENDS_${PN}-dev= "${PN}-slice"
RDEPENDS_${PN}-dev= "${PN}-slice"

# Glacier2
PACKAGES =+ "zeroc-glacier2"
FILES_zeroc-glacier2 += "${bindir}/glacier2router"

# IceGrid
PACKAGES =+ "zeroc-icegrid"
FILES_zeroc-icegrid += "${bindir}/icegrid*"

# IcePatch2
PACKAGES =+ "zeroc-icepatch2"
FILES_zeroc-icepatch2 += "${bindir}/icepatch2*"

# IceBox
PACKAGES =+ "zeroc-icebox"
FILES_zeroc-icebox += "${bindir}/icebox*"

# IceStorm
PACKAGES =+ "zeroc-icestorm"
FILES_zeroc-icestorm += "${bindir}/icestorm*"

# IceBridge
PACKAGES =+ "zeroc-icebridge"
FILES_zeroc-icebridge += "${bindir}/icebridge*"

# Python
PACKAGES += "${PN}-python3"
FILES_${PN}-python3 += "${PYTHON_SITEPACKAGES_DIR}"
RDEPENDS_${PN}-python3 = "${PN}-slice python3-core"

# Slice
PACKAGES =+ "${PN}-slice"
FILES_${PN}-slice += "${base_prefix}/usr/share/ice/slice ${base_prefix}/usr/share/slice"

# Utils
PACKAGES =+ "${PN}-utils"
FILES_${PN}-utils += "${bindir}/*admin"

# Add native and nativesdk support
BBCLASSEXTEND += "native nativesdk"
