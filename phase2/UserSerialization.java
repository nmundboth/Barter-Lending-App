package phase2;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

// Serialization template taken from
//https://howtodoinjava.com/java/collections/arraylist/serialize-deserialize-arraylist/

/**
 * <h1>User Serialization</h1>
 * <p>UserSerialization is used for serializing and deserializing a file that contains all User data.</p>
 */
public class UserSerialization
{
    /**
     * Takes an ArrayList of users (userBase from userCatalogue) and converts it to a serialized file called 'UserData'.
     * @param users the list of users to be serialized.
     */
    public void toSerialize(ArrayList<User> users) throws Exception
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
            ioe.printStackTrace(); //Remove once debugged
        }
    }

    /**
     * Deserializes a file, and converts it to an ArrayList of users for use in UserCatalogue
     * @return an ArrayList representing all users that have been registered to the system, as determined by the
     * serialized file.
     */
    public ArrayList<User> deserialize() throws Exception
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
            ioe.printStackTrace(); // Remove once debugged
        }
        catch (ClassNotFoundException c)
        {
            System.out.println("Class not found");
        }

        for (User name : userList) {
            System.out.println(name);
        }
        return userList;
    }
}

