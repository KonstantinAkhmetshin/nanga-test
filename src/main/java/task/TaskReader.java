package task;

import domain.TaskInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TaskReader
{
  private List<TaskInfo>    taskInfoList           = null;
  private List<TaskInfo>    aggregatedTaskInfoList = null;
  private Map<String,Float> reducedTaskToMemory    = null;

  public void startReading()
  {
    List<TaskInfo> taskInfoList = new ArrayList<>();
    List<TaskInfo> aggregatedTaskInfoList = new ArrayList<>();
    reducedTaskToMemory = new HashMap<>();

    try (BufferedReader input = getReader())
    {
      String line;
      boolean firstLineFlag = true;

      while( ( line = input.readLine() ) != null )
      {
        if( firstLineFlag )
        {
          firstLineFlag = false;
          continue;
        }

        TaskInfo taskInfo = parsProcessLine( line );
        aggregate(taskInfo);
        taskInfoList.add( taskInfo );

      }
    }
    catch( Exception err )
    {
      err.printStackTrace();
    }

    for( Map.Entry<String,Float> entry : reducedTaskToMemory.entrySet() )
    {
      String processName = entry.getKey();
      Float usedMemory = entry.getValue();

      aggregatedTaskInfoList.add( new TaskInfo( processName, usedMemory ) );
    }
    Collections.sort(taskInfoList);
    Collections.sort(aggregatedTaskInfoList);
    this.taskInfoList = taskInfoList;
    this.aggregatedTaskInfoList = aggregatedTaskInfoList;
  }

  public List<TaskInfo> getAggregatedTaskInfoList()
  {
    return aggregatedTaskInfoList;
  }

  public List<TaskInfo> getTaskInfoList()
  {
    return taskInfoList;
  }

  protected abstract String[] convertToProcessArray( String processLine );

  protected abstract Process getProcessFromOS() throws IOException;

  protected abstract TaskInfo parsProcessLine( String processLine );

  protected abstract BufferedReader getReader() throws IOException;

  private void aggregate( TaskInfo taskInfo )
  {
    Float currentUsedMemory = taskInfo.getUsedMemory();
    Float sum = reducedTaskToMemory.get( taskInfo.getProcessName() );
    if( sum == null )
    {
      sum = currentUsedMemory;
    }
    else
    {
      sum += currentUsedMemory;
    }

    reducedTaskToMemory.put( taskInfo.getProcessName(), sum );
  }

}
