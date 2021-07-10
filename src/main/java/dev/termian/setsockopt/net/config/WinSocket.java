package dev.termian.setsockopt.net.config;

import com.sun.jna.LastErrorException;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

interface WinSocket extends Library {
    WinSocket INSTANCE = Native.load("wsock32", WinSocket.class);

    /* https://docs.microsoft.com/en-us/windows/win32/api/ws2def/
     * https://docs.microsoft.com/en-us/windows/win32/winsock/ipproto-ip-socket-options
     * https://github.com/tpn/winsdk-10/blob/master/Include/10.0.10240.0/shared/ws2def.h */
    int IPPROTO_IP = 0;

    // https://docs.microsoft.com/en-us/windows/win32/winsock/ipproto-ip-socket-options
    // https://docs.microsoft.com/en-us/troubleshoot/windows/win32/header-library-requirement-socket-ipproto-ip
    // https://github.com/wine-mirror/wine/blob/master/include/winsock.h
    int IP_TTL = 7;
    int IP_DONTFRAGMENT = 9;

    int setsockopt(int socket, int level, int optname, Pointer optval, int optlen) throws LastErrorException;

}