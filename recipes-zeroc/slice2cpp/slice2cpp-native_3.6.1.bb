SUMMARY  = "Compiles Slice files to C++"
HOMEPAGE = "https://zeroc.com"
SECTION  = "libs"

LICENSE  = "GPLv2"
LIC_FILES_CHKSUM = "file://ICE_LICENSE;md5=9fde27d262eab318820e1d5d3a8a15c6"

PR = "r0"
PV = "3.6.1-rc0"
MCPPV = "2.7.2-3ice"

SRC_URI = "https://github.com/zeroc-ice/ice/archive/v${PV}.tar.gz;downloadfilename=ice-${PV}.tar.gz;name=ice \
           https://github.com/zeroc-ice/mcpp/archive/v${MCPPV}.tar.gz;downloadfilename=mcpp-${MCPPV}.tar.gz;name=mcpp"

SRC_URI[ice.md5sum] = "557e7464c11d4ebc64c13c7c474ed1d3"
SRC_URI[ice.sha256sum] = "4483044027b49e59453fa68dc232302dbc757a5eeaa2d30e74b890b49e5c2152"
SRC_URI[mcpp.md5sum] = "f68ff68d64093fddc3cb7be76ac4755e"
SRC_URI[mcpp.sha256sum] = "692f2a3b3f029afc72f2bee60a608d48af4299365df76635331009b583819995"

inherit native

S = "${WORKDIR}/ice-${PV}"
mcppdir = "${WORKDIR}/mcpp-${MCPPV}"

EXTRA_OEMAKE = "'STATICLIBS=yes' 'MCPP_HOME=${mcppdir}' 'GCC_COMPILER=yes'"

do_compile() {
    cd ${mcppdir}
    oe_runmake

    cd ${S}/cpp/src
    oe_runmake slice2cpp
}

do_install () {
    install -d ${D}${bindir}/
    mv ${S}/cpp/bin/slice2cpp ${D}${bindir}
}

BBCLASSEXTEND += "native nativesdk"
