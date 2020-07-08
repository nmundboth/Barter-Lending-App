package phase1;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

// Serialization template taken from
//https://howtodoinjava.com/java/collections/arraylist/serialize-deserialize-arraylist/

public class UserSerialization
{
    public static void toSerialize(ArrayList<User> users) throws Exception
    {
        ArrayList<User> userList = new ArrayList<User>(users);
        try
        {
            FileOutputStream fos = new FileOutputStream("UserData");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(userList);
            oos.close();
            fos.close();
        }
        catch (IOException ioe)
        {
            System.out.println("IO Error");
        }
    }

    public static void deserialize() throws Exception
    {
        ArrayList<User> userList = new ArrayList<User>();

        try
        {
            FileInputStream fis = new FileInputStream("UserData");
            ObjectInputStream ois = new ObjectInputStream(fis);

            userList = (ArrayList<User>) ois.readObject();

            ois.close();
            fis.close();
        }
        catch (IOException ioe)
        {
            System.out.println("IO Error");
        }
        catch (ClassNotFoundException c)
        {
            System.out.println("Class not found");
        }

        for (User name : userList) {
            System.out.println(name);
        }
    }
}

