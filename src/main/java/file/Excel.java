package file;

import com.smartxls.ChartFormat;
import com.smartxls.ChartShape;
import com.smartxls.WorkBook;
import domain.TaskInfo;

import java.awt.Color;
import java.io.File;
import java.util.List;

public class Excel
{
  private static Excel instance = null;

  private Excel() {
  }

  public static Excel getInstance()
  {
    if( instance == null )
    {
        instance = new Excel();
    }
    return instance;
  }

  public void writeReport( List<TaskInfo> taskInfoList, File file )
  {
    WorkBook workBook = new WorkBook();

    try
    {
      workBook.setText( 0, 0, "PID" );
      workBook.setText( 0, 1, "Process Name" );
      workBook.setText( 0, 2, "Used Memory" );

      int row = 0;
      for( int index = 0 ; index < taskInfoList.size() ; index++ )
      {
        row = index + 1;
        TaskInfo taskInfo = taskInfoList.get( index );
        if( taskInfo.getPid() != null )
        {
          workBook.setText( row, 0, taskInfo.getPid() );
        }
        workBook.setText( row, 1, taskInfo.getProcessName() );

        workBook.setNumber( row, 2, taskInfo.getUsedMemory() );
      }

// auto fill the range with the first cell's formula or data
      workBook.editCopyRight();

      int left = 5;
      int top = 0;
      int right = 15;
      int bottom = 13;

// create chart with it's location
      ChartShape chart = workBook.addChart( left, top, right, bottom );
      chart.setChartType( ChartShape.Column );
// link data source, link each series to columns(true to rows).
      chart.setLinkRange( "Sheet1!$b$1:$c$" + row, false );
// set axis title

      chart.setTitle( "Used memory to task." );
// set plot area's color to darkgray
      ChartFormat chartFormat = chart.getPlotFormat();
      chartFormat.setSolid();
      chartFormat.setForeColor( Color.red.getRGB() );
      chart.setPlotFormat( chartFormat );

// set chart title's font property
      ChartFormat titleformat = chart.getTitleFormat();
      titleformat.setFontSize( 14 * 20 );
      titleformat.setFontUnderline( true );
      titleformat.setTextRotation( 90 );
      chart.setTitleFormat( titleformat );

      workBook.writeXLSX( file.getAbsolutePath() );
    }
    catch( Exception ex )
    {
      ex.printStackTrace();
    }

  }

}
