package task.factory;

import task.MacTaskReader;
import task.TaskReader;
import task.WindowsTaskReader;

public class TaskFactory
{
  private static String OS = System.getProperty("os.name").toLowerCase();

  public static TaskReader getTaskReader()
  {
    if(OS.contains("win"))
    {
      return WindowsTaskReader.getInstance();
    }
    if(OS.contains("mac"))
    {
      return MacTaskReader.getInstance();
    }
    throw new RuntimeException("Task reader has no implementation for your OS");
  }

}
