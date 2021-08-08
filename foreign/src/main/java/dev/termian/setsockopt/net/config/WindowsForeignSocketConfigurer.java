package dev.termian.setsockopt.net.config;

import dev.termian.setsockopt.net.impl.FileDescriptorWrapper;
import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.FunctionDescriptor;
import jdk.incubator.foreign.LibraryLookup;
import jdk.incubator.foreign.MemoryAccess;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.MemorySegment;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

public class WindowsForeignSocketConfigurer extends SocketConfigurer {

    private static final MethodHandle setsockopt = CLinker.getInstance().downcallHandle(
            LibraryLookup.ofDefault().lookup("setsockopt").orElseThrow(ExceptionInInitializerError::new),
            MethodType.methodType(int.class, int.class, int.class, int.class, MemoryAddress.class, int.class),
            FunctionDescriptor.of(CLinker.C_INT, CLinker.C_INT, CLinker.C_INT, CLinker.C_INT, CLinker.C_POINTER, CLinker.C_INT)
    );

    private static final MethodHandle WSAGetLastError = CLinker.getInstance().downcallHandle(
            LibraryLookup.ofDefault().lookup("WSAGetLastError").orElseThrow(ExceptionInInitializerError::new),
            MethodType.methodType(int.class),
            FunctionDescriptor.of(CLinker.C_INT)
    );

    private static void setSockOpt(FileDescriptorWrapper fileDescriptor, int level, int optionName, MemorySegment optionValue) throws Throwable {
        Integer result = (Integer) setsockopt.invoke(
                fileDescriptor.getFd(),
                level,
                optionName,
                optionValue.address(),
                4
        );
        if (result != 0) {
            throw new IOException("Error code: " + WSAGetLastError.invokeWithArguments());
        }
    }

    public WindowsForeignSocketConfigurer(Configuration configuration) {
        super(configuration);
    }

    @Override
    public void setDontFragment(FileDescriptorWrapper fileDescriptor, boolean dontFragment) {
        try (MemorySegment optionValue = MemorySegment.allocateNative(32)) {
            MemoryAccess.setIntAtIndex(optionValue, 0, dontFragment ? 1 : 0);
            setSockOpt(fileDescriptor, WinSocket.IPPROTO_IP, WinSocket.IP_DONTFRAGMENT, optionValue);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public void setTtl(FileDescriptorWrapper fileDescriptor, int ttl) {
        try (MemorySegment optionValue = MemorySegment.allocateNative(32)) {
            MemoryAccess.setIntAtIndex(optionValue, 0, ttl);
            setSockOpt(fileDescriptor, WinSocket.IPPROTO_IP, WinSocket.IP_TTL, optionValue);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

}
