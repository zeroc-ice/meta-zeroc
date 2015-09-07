SUMMARY  = "ZeroC Ice for Yocto"
DESCRIPTION = "Comprehensive RPC framework"
HOMEPAGE = "https://zeroc.com"
SECTION  = "libs"

LICENSE  = "GPLv2"
LIC_FILES_CHKSUM = "file://ICE_LICENSE;md5=b736c5ad38678f3d541b465ac944711f"

DEPENDS  = "openssl bzip2 python"
DEPENDS_append_class-target = " zeroc-ice-native"
DEPENDS_append_class-nativesdk = " zeroc-ice-native"
RDEPENDS_${PN} = "openssl bzip2"

SRC_URI = "git://github.com/zeroc-ice/icee.git;protocol=http;branch=3.6"
SRCREV = "850012d2401d6761e5ddfa844e6e4ebe0b936c8e"

ICE_VERSION = "3.6.1"

PV = "${ICE_VERSION}-rc"
PR = "4"

S = "${WORKDIR}/git"
B = "${WORKDIR}/git"
MCPP_DIR = "${S}/mcpp"

EXTRA_OEMAKE = "'ICEE_TARGET_OS=yocto' 'OPTIMIZE=yes'"
EXTRA_OEMAKE_append_class-target = " ICE_HOME=${STAGING_DIR_NATIVE}/usr"
EXTRA_OEMAKE_append_class-nativesdk = " ICE_HOME=${STAGING_DIR_NATIVE}/usr"

inherit python-dir

do_configure () {
    git submodule update --init
}

do_compile_prepend_class-native () {
    oe_runmake -C${MCPP_DIR}

    for dir in IceUtil Slice slice2cpp slice2py; do
        oe_runmake -C${S}/ice/cpp/src/${dir} STATICLIBS=yes MCPP_HOME=${MCPP_DIR} GCC_COMPILER=yes
    done
}

do_compile () {
    oe_runmake dist python_include_dir=${STAGING_INCDIR}/${PYTHON_DIR}
}

do_install () {
    oe_runmake install DESTDIR=${D} prefix=${prefix}
}

# Add slice compilers and -slice dependency to -dev
FILES_${PN}-dev += "${bindir}/slice2*"
DEPENDS_${PN}-dev= "${PN}-slice"
RDEPENDS_${PN}-dev= "${PN}-slice"

# Add Python debug files
FILES_${PN}-dbg += "${PYTHON_SITEPACKAGES_DIR}/.debug"

# Slice Package
PACKAGES =+ "zeroc-ice-slice"
FILES_${PN}-slice += "${base_prefix}/usr/share/Ice-${ICE_VERSION}"

# Glacier2 Package
PACKAGES =+ "zeroc-glacier2"
FILES_zeroc-glacier2 += "${bindir}/glacier2router"

# IceBox Package
PACKAGES =+ "zeroc-icebox"
FILES_zeroc-icebox += "${bindir}/icebox"

# Utils Package
PACKAGES =+ "${PN}-utils"
FILES_${PN}-utils += "${bindir}/iceboxadmin"

# Python Package
PACKAGES += "${PN}-python"
FILES_${PN}-python += "${PYTHON_SITEPACKAGES_DIR}"
RDEPENDS_${PN}-python = "${PN}-slice python-core"

# Add native and nativesdk support
BBCLASSEXTEND += "native nativesdk"
