package com.lelazy.host.classes;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class pingzireadstream extends ObjectInputStream {
    public pingzireadstream(InputStream in) throws IOException {
        super(in);
    }

    protected pingzireadstream() throws IOException, SecurityException {
    }

    @Override
    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        ObjectStreamClass objectStreamClass=super.readClassDescriptor();
        if (objectStreamClass.getName().startsWith("com.lelazy.yuanting.classes.pingzi")){
            Class type=Class.forName(objectStreamClass.getName().replace("com.lelazy.yuanting.classes","com.lelazy.host.classes"));
            return ObjectStreamClass.lookup(type);
        }
        /*return super.readClassDescriptor();*/
        return objectStreamClass;
    }
}
