package file;

import domain.TaskInfoHolder;
import domain.TaskInfo;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

public class Xml
{
  private static Xml instance = null;

  private JAXBContext         jaxbContext;
  private Marshaller          jaxbMarshaller;
  private Unmarshaller        jaxbUnmarshaller;

  private Xml() throws JAXBException
  {
    jaxbContext      = JAXBContext.newInstance( TaskInfoHolder.class );
    jaxbMarshaller   = jaxbContext.createMarshaller();
    jaxbUnmarshaller = jaxbContext.createUnmarshaller();
  }

  // NOT THREAD-SAFE.
  public static Xml getInstance()
  {
    if( instance == null )
    {
      try
      {
        instance = new Xml();
      }
      catch( JAXBException e )
      {
        throw new RuntimeException( "Can't create new instance of " + Xml.class );
      }
    }
    return instance;
  }

  public void writeToFile( List<TaskInfo> tasks, File file )
  {
    try
    {
      // output pretty printed
      jaxbMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
      jaxbMarshaller.marshal( new TaskInfoHolder( tasks ), file );
    }
    catch( JAXBException e )
    {
      throw new RuntimeException("Can't write XML");
    }

  }

  public List<TaskInfo> readFromFile( File file )
  {
    try
    {
      TaskInfoHolder taskInfoHolder = (TaskInfoHolder)jaxbUnmarshaller.unmarshal( file );
      return taskInfoHolder.getTaskInfoList();
    }
    catch( JAXBException e )
    {
      throw new RuntimeException("Can't read XML");
    }
  }

}
