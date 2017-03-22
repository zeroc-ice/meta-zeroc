SUMMARY  = "ZeroC Ice for Yocto"
DESCRIPTION = "Comprehensive RPC framework"
HOMEPAGE = "https://zeroc.com"
SECTION  = "libs"

LICENSE  = "GPLv2"
LIC_FILES_CHKSUM = "file://ICE_LICENSE;md5=5edee2df5627c95e640d5e1c65323adb \
                    file://LICENSE;md5=21174f1da53145d75abea1badd2cbab4"

PV = "master"
PR = "r0"

SRC_URI = "https://github.com/${BPN}/ice/archive/${PV}.tar.gz"

# SRC_URI[md5sum] = ""
# SRC_URI[sha256sum] = ""

S = "${WORKDIR}/ice-${PV}"
B = "${WORKDIR}/ice-${PV}"
# DEFAULT_PREFERENCE = "-1"

inherit bluetooth python-dir

BLUEZ_DEPS = "${BLUEZ} dbus-glib expat"
DEPENDS  = "openssl bzip2 python mcpp lmdb ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', '${BLUEZ_DEPS}', '', d)}"
RDEPENDS_${PN} = "openssl bzip2"

python () {
    import re
    m = re.search('^([a-z0-9]+)-.*', d.getVar("CXX", True))
    if m:
        d.setVar('PLATFORM', m.group(1))
}

# OECORE_SDK_VERSION is always set in an SDK. To get the Ice build system to
# detect a Yocto/OE build just need to to be set here.
EXTRA_OEMAKE = "'OECORE_SDK_VERSION=yes' \
                'CONFIGS=all' \
                'LANGUAGES=cpp python' \
                "${PLATFORM}_excludes=${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', '', 'IceBT', d)}"\
                'install_pythondir=${PYTHON_SITEPACKAGES_DIR}' \
                'PYTHON_LIB_DIR=${STAGING_LIBDIR}' \
                'PYTHON_LIB_SUFFIX=${PYTHON_ABI}' \
                'PYTHON_BASE_VERSION=${PYTHON_BASEVERSION}' \
                'PYTHON_INCLUDE_DIR=${STAGING_INCDIR}/${PYTHON_DIR}'"

do_compile () {
    oe_runmake srcs
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

# Python
PACKAGES += "${PN}-python"
FILES_${PN}-python += "${PYTHON_SITEPACKAGES_DIR}"
RDEPENDS_${PN}-python = "${PN}-slice python-core"

# Slice
PACKAGES =+ "${PN}-slice"
FILES_${PN}-slice += "${base_prefix}/usr/share/ice/slice /usr/share/slice"

# Utils
PACKAGES =+ "${PN}-utils"
FILES_${PN}-utils += "${bindir}/*admin"

# Add native and nativesdk support
BBCLASSEXTEND += "native nativesdk"
