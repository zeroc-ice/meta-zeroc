SUMMARY  = "Ice-E"
DESCRIPTION = "Ice-E brings Ice for C++ and Ice for Python to embedded devices"
HOMEPAGE = "https://zeroc.com"
SECTION  = "libs"

LICENSE  = "GPLv2"
LIC_FILES_CHKSUM = "file://ICE_LICENSE;md5=b736c5ad38678f3d541b465ac944711f"

DEPENDS  = "openssl bzip2 python"
DEPENDS_append_class-target = " icee-native"
DEPENDS_append_class-nativesdk = " icee-native"
RDEPENDS_${PN} = "openssl bzip2"

SRC_URI = "git://github.com/zeroc-ice/icee.git;protocol=http;branch=3.6"
SRCREV = "18b0fd1b5e0af387787cb06746cd4ac5903ebfc3"

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

# Ensure Slice files are in dev package
FILES_${PN}-dev += "${bindir}/slice2* ${base_prefix}/usr/share/Ice-${PV}"

# Add Python debug files
FILES_${PN}-dbg += "${PYTHON_SITEPACKAGES_DIR}/.debug"

# Glacier2 Package
PACKAGES =+ "${PN}-glacier2"
FILES_${PN}-glacier2 += "${bindir}/glacier2router"

# IceBox Package
PACKAGES =+ "${PN}-icebox"
FILES_${PN}-icebox += "${bindir}/icebox*"

# Python Package
PACKAGES += "${PN}-python"
FILES_${PN}-python += "${PYTHON_SITEPACKAGES_DIR}"
RDEPENDS_${PN}-python = "python"

BBCLASSEXTEND += "native nativesdk"
