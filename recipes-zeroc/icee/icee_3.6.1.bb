SUMMARY  = "Ice-E brings Ice for C++ and Ice for Python to embedded devices"
HOMEPAGE = "https://zeroc.com"
DEPENDS  = "openssl bzip2 slice2cpp-native"
SECTION  = "libs"

LICENSE  = "GPLv2"
LIC_FILES_CHKSUM = "file://ICE_LICENSE;md5=b736c5ad38678f3d541b465ac944711f"

PR = "r0"

SRC_URI = "git://github.com/zeroc-ice/icee.git;protocol=http;branch=3.6"
SRCREV = "560397c09f2b7f2e258138904469c8e69b97755b"

S = "${WORKDIR}/git/"
B = "${WORKDIR}/git/"

EXTRA_OEMAKE = "'ICEE_TARGET_OS=yocto'"

# We need to set this outside EXTRA_OEMAKE otherwise the build system is
# unhappy about using paths on the host
export ICE_HOME = "${STAGING_DIR_NATIVE}/usr"

do_configure () {
    git submodule update --init
}

do_compile () {
    echo ${ICE_HOME}
    oe_runmake dist
}

do_install () {
    oe_runmake install DESTDIR=${D} SBINDIR=${sbindir} MANDIR=${mandir} INCLUDEDIR=${includedir}
}

# Ensure Slice files are in -dev package
FILES_${PN}-dev += "/usr/share/Ice-3.6.1/"

# Glacier2 Package
PACKAGES =+ "${PN}-glacier2"
FILES_${PN}-glacier2 += "${bindir}/glacier2router"
RDEPENDS_${PN}-glacier2 = "openssl bzip2"

BBCLASSEXTEND += "native nativesdk"
