package task;

import domain.TaskInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class WindowsTaskReader extends TaskReader
{
  private static final int TASK_NAME_INDEX   = 0;
  private static final int PID_INDEX         = 1;
  private static final int USED_MEMORY_INDEX = 4;
  private static final String WINDOWS_ENCODING = "Cp866";

  private static WindowsTaskReader instance = null;

  private WindowsTaskReader() {
  }

  public static WindowsTaskReader getInstance()
  {
    if( instance == null )
    {
      instance = new WindowsTaskReader();
    }
    return instance;
  }

  @Override
  protected Process getProcessFromOS() throws IOException
  {
    return Runtime.getRuntime().exec( System.getenv( "windir" ) + "\\system32\\" + "tasklist.exe /fo csv /nh" );
  }

  @Override
  public TaskInfo parsProcessLine( String processLine )
  {
    String[] processArray = convertToProcessArray(processLine);
    String usedMemoryString = processArray[USED_MEMORY_INDEX].substring(0, processArray[USED_MEMORY_INDEX].length() - 2);

    Float usedMemoryFloat = Float.parseFloat( usedMemoryString.replaceAll("[^\\d.]", "") );

    TaskInfo taskInfo = new TaskInfo( processArray[TASK_NAME_INDEX], processArray[PID_INDEX], usedMemoryFloat );
    return taskInfo;
  }

  @Override
  protected BufferedReader getReader() throws IOException
  {
    return new BufferedReader( new InputStreamReader( getProcessFromOS().getInputStream(), WINDOWS_ENCODING ) );
  }

  protected String[] convertToProcessArray( String processLine )
  {
    String[] newLine = processLine.trim().replaceAll( "\"|\\s+", "" ).split( "," );
    return newLine;
  }

}
