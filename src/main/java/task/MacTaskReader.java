package task;

import domain.TaskInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MacTaskReader extends TaskReader
{
  private static final int USER_NAME_INDEX   = 0;
  private static final int PID_INDEX         = 1;
  private static final int USED_MEMORY_INDEX = 3;

  private static MacTaskReader instance = null;

  private MacTaskReader() {
  }

  public static MacTaskReader getInstance()
  {
    if( instance == null )
    {
      instance = new MacTaskReader();
    }
    return instance;
  }


  @Override
  protected Process getProcessFromOS() throws IOException
  {
    return Runtime.getRuntime().exec( "ps aux" );
  }

  @Override
  protected TaskInfo parsProcessLine( String processLine )
  {
    String[] processArray = convertToProcessArray( processLine );
    return new TaskInfo( processArray[USER_NAME_INDEX], processArray[PID_INDEX], Float.parseFloat( processArray[USED_MEMORY_INDEX] ) );
  }

  protected String[] convertToProcessArray( String processLine )
  {
    return processLine.trim().replaceAll( " +", " " ).split( " " );
  }

  @Override
  protected BufferedReader getReader() throws IOException
  {
    return new BufferedReader( new InputStreamReader( getProcessFromOS().getInputStream() ) );
  }
}
