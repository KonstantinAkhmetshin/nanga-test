package domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement( name = "tasks" )
public class TaskInfoHolder
{
  public TaskInfoHolder()
  {
  }

  public TaskInfoHolder( List<TaskInfo> taskInfoList )
  {
    this.taskInfoList = taskInfoList;
  }

  private List<TaskInfo> taskInfoList;

  public List<TaskInfo> getTaskInfoList()
  {
    return taskInfoList;
  }

  @XmlElement( name = "task" )
  public TaskInfoHolder setTaskInfoList( List<TaskInfo> taskInfoList )
  {
    this.taskInfoList = taskInfoList;
    return this;
  }
}
