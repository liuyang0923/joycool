package net.joycool.wap.call;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;


public class CallClassLoader extends ClassLoader {

	public static class ResourceEntry {
		public long lastModified = -1;

		public byte[] binaryContent = null;
		
		public int size;

		public Class loadedClass = null;
	}

	protected HashMap resourceEntries = new HashMap();

	protected HashMap notFoundResources = new HashMap();

	private ClassLoader parent = null;
	private ClassLoader system = null;

	public CallClassLoader() {
		super(CallClassLoader.class.getClassLoader());
		this.parent = CallClassLoader.class.getClassLoader();
		system = getSystemClassLoader();
	}

	protected synchronized Class loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		Class clazz = findLoadedClass0(name);
        if (clazz != null) {
            if (resolve)
                resolveClass(clazz);
            return (clazz);
        }
		
        try {
            clazz = system.loadClass(name);
            if (clazz != null) {
                if (resolve)
                    resolveClass(clazz);
                return (clazz);
            }
        } catch (ClassNotFoundException e) {
            // Ignore
        }

		if(!name.startsWith("net.joycool.wap.call") || name.startsWith("net.joycool.wap.call.Call")){
			return parent.loadClass(name);
		}
		clazz = findClass(name);
		if (clazz != null && resolve)
			resolveClass(clazz);
		return clazz;
	}

	protected Class findClass(String name) throws ClassNotFoundException {
		return findClassInternal(name);
	}

	private SecurityManager securityManager = null;

	protected Class findClassInternal(String name) throws ClassNotFoundException {

		String tempPath = name.replace('.', '/');
		String classPath = tempPath + ".class";

		ResourceEntry entry = null;

		entry = findResourceInternal(name, classPath);

		if ((entry == null) || (entry.binaryContent == null))
			throw new ClassNotFoundException(name);

		Class clazz = entry.loadedClass;
		if (clazz != null)
			return clazz;

		if (entry.loadedClass == null) {
			synchronized (this) {
				if (entry.loadedClass == null) {
					clazz = defineClass(name, entry.binaryContent, 0, entry.binaryContent.length);
					entry.loadedClass = clazz;
					entry.binaryContent = null;
				} else {
					clazz = entry.loadedClass;
				}
			}
		} else {
			clazz = entry.loadedClass;
		}

		return clazz;

	}
	
    protected Class findLoadedClass0(String name) {

        ResourceEntry entry = (ResourceEntry) resourceEntries.get(name);
        if (entry != null) {
            return entry.loadedClass;
        }
        return (null);  // FIXME - findLoadedResource()

    }

	protected ResourceEntry findResourceInternal(String name, String path) {

		if ((name == null) || (path == null))
			return null;

		ResourceEntry entry = (ResourceEntry) resourceEntries.get(name);
		if (entry != null)
			return entry;

		int contentLength = 0;
		entry = new ResourceEntry();
		InputStream binaryStream;
		try {
			binaryStream = new FileInputStream(getClass().getResource("/" + path).getPath());
		} catch (FileNotFoundException e1) {
			return null;
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
        if (binaryStream != null) {

            byte[] binaryContent = new byte[4096];
            
            try {
                int pos = 0;

                while (true) {
                    int n = binaryStream.read(binaryContent, 0,
                                              4096);
                    if (n <= 0)
                        break;
                    os.write(binaryContent, 0, n);
                    contentLength += n;
                }
                binaryStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            entry.binaryContent = os.toByteArray();
            entry.size = contentLength;

        }
        
        synchronized (resourceEntries) {
            // Ensures that all the threads which may be in a race to load
            // a particular class all end up with the same ResourceEntry
            // instance
            ResourceEntry entry2 = (ResourceEntry) resourceEntries.get(name);
            if (entry2 == null) {
                resourceEntries.put(name, entry);
            } else {
                entry = entry2;
            }
        }
        
		return entry;

	}
}
