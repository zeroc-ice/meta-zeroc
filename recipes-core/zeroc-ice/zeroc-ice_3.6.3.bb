SUMMARY  = "ZeroC Ice for Yocto"
DESCRIPTION = "Comprehensive RPC framework"
HOMEPAGE = "https://zeroc.com"
SECTION  = "libs"

LICENSE  = "GPLv2"
LIC_FILES_CHKSUM = "file://ICE_LICENSE;md5=eda3d1dc0f8f1e70378830dacd0c44d5"

DEPENDS  = "openssl bzip2 python"
DEPENDS_append_class-target = " zeroc-ice-native"
DEPENDS_append_class-nativesdk = " zeroc-ice-native"
RDEPENDS_${PN} = "openssl bzip2"

SRC_URI = "git://github.com/zeroc-ice/icee.git;protocol=http;branch=3.6"
SRCREV = "eaddf95b1a430508f4857e04f62d252b972d9186"

ICE_VERSION = "3.6.3"
PV = "${ICE_VERSION}"
PR = "r0"

S = "${WORKDIR}/git"
B = "${WORKDIR}/git"
MCPP_DIR = "${S}/mcpp"

EXTRA_OEMAKE = "'ICEE_TARGET_OS=yocto' 'OPTIMIZE=yes' 'NOFREEZE=yes'"
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
    oe_runmake dist CPP11=yes
}

do_install () {
    oe_runmake install DESTDIR=${D} prefix=${prefix}
    oe_runmake install DESTDIR=${D} prefix=${prefix} CPP11=yes

    rm -f ${D}${libdir}/IcePy*
    rm -f ${D}${PYTHON_SITEPACKAGES_DIR}/IcePy.so
    rm -f ${D}${PYTHON_SITEPACKAGES_DIR}/IcePy.so.36
}

# Add slice compilers and -slice dependency to -dev and add c++11 library symlinks
FILES_${PN}-dev += "${bindir}/slice2* ${libdir}/c++11/*.so"
DEPENDS_${PN}-dev= "${PN}-slice"
RDEPENDS_${PN}-dev= "${PN}-slice"

FILES_${PN}-staticdev += "${libdir}/c++11/*.a"

# Add Python debug files to -dbg
FILES_${PN}-dbg += "${PYTHON_SITEPACKAGES_DIR}/.debug"

# Slice
PACKAGES =+ "${PN}-slice"
FILES_${PN}-slice += "${base_prefix}/usr/share/Ice-${ICE_VERSION}"

# Glacier2
PACKAGES =+ "zeroc-glacier2"
FILES_zeroc-glacier2 += "${bindir}/glacier2router"

# IceBox
PACKAGES =+ "zeroc-icebox"
FILES_zeroc-icebox += "${bindir}/icebox"

# Utils
PACKAGES =+ "${PN}-utils"
FILES_${PN}-utils += "${bindir}/iceboxadmin"

# Python
PACKAGES += "${PN}-python"
FILES_${PN}-python += "${PYTHON_SITEPACKAGES_DIR}"
RDEPENDS_${PN}-python = "${PN}-slice python-core"

# Add native and nativesdk support
BBCLASSEXTEND += "native nativesdk"
