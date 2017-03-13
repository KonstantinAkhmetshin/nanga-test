package domain;

import javax.xml.bind.annotation.XmlElement;

public class TaskInfo implements Comparable<TaskInfo>
{
  private String pid;
  private String processName;
  private Float  usedMemory;

  public TaskInfo()
  {
  }

  public TaskInfo( String processName, Float usedMemory )
  {
    this.processName = processName;
    this.usedMemory = usedMemory;
  }

  public TaskInfo( String processName, String pid, Float usedMemory )
  {
    this.pid = pid;
    this.usedMemory = usedMemory;
    this.processName = processName;
  }

  public String getPid()
  {
    return pid;
  }

  public TaskInfo setPid( String pid )
  {
    this.pid = pid;
    return this;
  }

  public Float getUsedMemory()
  {
    return usedMemory;
  }

  @XmlElement( name = "memory" )
  public TaskInfo setUsedMemory( Float usedMemory )
  {
    this.usedMemory = usedMemory;
    return this;
  }

  public String getProcessName()
  {
    return processName;
  }

  @XmlElement( name = "name" )
  public TaskInfo setProcessName( String processName )
  {
    this.processName = processName;
    return this;
  }

  @Override
  public String toString()
  {
    return "ProcessInfo{" + "processName='" + processName + '\'' + ", pid='" + pid + '\'' + ", usedMemory='" + usedMemory + '\'' + '}' + "\n";
  }

  @Override
  public boolean equals( Object o )
  {
    if( this == o )
      return true;
    if( o == null || getClass() != o.getClass() )
      return false;

    TaskInfo that = (TaskInfo)o;

    if( pid != null ? !pid.equals( that.pid ) : that.pid != null )
      return false;
    if( usedMemory != null ? !usedMemory.equals( that.usedMemory ) : that.usedMemory != null )
      return false;
    return ! ( processName != null ? !processName.equals( that.processName ) : that.processName != null );

  }

  @Override
  public int hashCode()
  {
    int result = pid != null ? pid.hashCode() : 0;
    result = 31 * result + ( usedMemory != null ? usedMemory.hashCode() : 0 );
    result = 31 * result + ( processName != null ? processName.hashCode() : 0 );
    return result;
  }

  public int compareTo( TaskInfo o )
  {
    if( this.usedMemory > o.getUsedMemory() )
      return -1;
    if( this.usedMemory < o.getUsedMemory() )
      return 1;
    return 0;
  }
}
