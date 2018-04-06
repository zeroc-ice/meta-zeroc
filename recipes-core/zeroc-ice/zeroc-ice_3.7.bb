SUMMARY  = "ZeroC Ice for Yocto"
DESCRIPTION = "Comprehensive RPC framework"
HOMEPAGE = "https://zeroc.com"
SECTION  = "libs"

LICENSE  = "GPLv2"
LIC_FILES_CHKSUM = "file://ICE_LICENSE;md5=3679fa8e314664494eac16771c840e06 \
                    file://LICENSE;md5=1b65bb9598f16820aab2ae1dd2a51f9f"

SRCREV  = "${AUTOREV}"

PV = "3.7.1+git${SRCPV}"
PR = "r0"

SRC_URI = "git://github.com/zeroc-ice/ice;branch=3.7"

S = "${WORKDIR}/git"
B = "${WORKDIR}/git"

inherit bluetooth pythonnative pkgconfig

BLUEZ_DEPS = "${BLUEZ} dbus-glib"
DEPENDS  = " openssl bzip2 python mcpp lmdb expat ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', '${BLUEZ_DEPS}', '', d)}"
RDEPENDS_${PN} = "openssl bzip2"

python () {
    import re
    m = re.search('^([a-z0-9_]+)-.*', d.getVar("CXX", True))
    if m:
        d.setVar('PLATFORM', m.group(1))
}

# OECORE_SDK_VERSION is always set in an SDK. To get the Ice build system to
# detect a Yocto/OE build just need to to be set here.
EXTRA_OEMAKE = "'OECORE_SDK_VERSION=yes' \
                'CONFIGS=all' \
                'LANGUAGES=cpp python' \
                'USR_DIR_INSTALL=yes' \
                "${PLATFORM}_excludes=${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', '', 'IceBT', d)}"\
                'install_pythondir=${PYTHON_SITEPACKAGES_DIR}'"

do_compile () {
    oe_runmake V=1 srcs
}

do_install () {
    oe_runmake install DESTDIR=${D} prefix=${prefix}
}
FILES_${PN} += "${base_prefix}/usr/share/ice/templates.xml \
                ${base_prefix}/usr/share/ice/ICE_LICENSE \
                ${base_prefix}/usr/share/ice/LICENSE"

# Add slice compilers and -slice dependency to -dev
FILES_${PN}-dev += "${bindir}/slice2*"
DEPENDS_${PN}-dev= "${PN}-slice"
RDEPENDS_${PN}-dev= "${PN}-slice"

# Add Python debug files to -dbg
FILES_${PN}-dbg += "${PYTHON_SITEPACKAGES_DIR}/.debug"

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
PACKAGES += "${PN}-python"
FILES_${PN}-python += "${PYTHON_SITEPACKAGES_DIR}"
RDEPENDS_${PN}-python = "${PN}-slice python-core"

# Slice
PACKAGES =+ "${PN}-slice"
FILES_${PN}-slice += "${base_prefix}/usr/share/ice/slice ${base_prefix}/usr/share/slice"

# Utils
PACKAGES =+ "${PN}-utils"
FILES_${PN}-utils += "${bindir}/*admin"

# Add native and nativesdk support
BBCLASSEXTEND += "native nativesdk"
