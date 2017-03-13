import domain.TaskInfo;
import file.Excel;
import file.Xml;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import task.factory.TaskFactory;
import task.TaskReader;

import java.io.File;
import java.util.List;

public class Main extends Application
{
  private TableView<TaskInfo> mainTable    = new TableView<>();
  private TableView<TaskInfo> compareTable = new TableView<>();

  private static TaskReader   taskReader   = TaskFactory.getTaskReader();

  public static void main( String[] args )
  {
    taskReader.startReading();
    Application.launch( args );
  }

  @Override
  public void start( final Stage stage ) throws Exception
  {
    Scene scene = new Scene( new Group() );
    stage.setTitle( "Task List Viewer" );
    stage.setWidth( 900 );
    stage.setHeight( 600 );

    initTableColumns( mainTable );
    initTableColumns( compareTable );

    populateTable( mainTable, taskReader.getTaskInfoList() );

    final Button reduceButton = new Button( "Reduced Task List" );
    reduceButton.setMaxSize( Double.MAX_VALUE, Double.MAX_VALUE );

    final Button exportXmlButton = new Button( "Export XML" );
    exportXmlButton.setMaxSize( Double.MAX_VALUE, Double.MAX_VALUE );

    final Button exportExcelButton = new Button( "Export Excel" );
    exportExcelButton.setMaxSize( Double.MAX_VALUE, Double.MAX_VALUE );

    final Button importXmlButton = new Button( "Import XML" );
    importXmlButton.setMaxSize( Double.MAX_VALUE, Double.MAX_VALUE );

    TilePane tileButtons = new TilePane( Orientation.HORIZONTAL );
    tileButtons.setPadding( new Insets( 20, 10, 20, 0 ) );
    tileButtons.setHgap( 10.0 );
    tileButtons.getChildren().addAll( reduceButton, exportXmlButton, exportExcelButton, importXmlButton );

    TilePane tileTables = new TilePane( Orientation.HORIZONTAL );
    tileTables.setPadding( new Insets( 20, 10, 20, 0 ) );
    tileTables.setHgap( 10.0 );
    tileTables.getChildren().addAll( mainTable, compareTable );

    final VBox vbox = new VBox();
    vbox.setSpacing( 5 );
    vbox.setPadding( new Insets( 10, 0, 0, 10 ) );
    vbox.getChildren().addAll( tileTables, tileButtons );

    ( (Group)scene.getRoot() ).getChildren().addAll( vbox );

    stage.setScene( scene );
    stage.show();

    // Listeners

    final FlagHolder flagHolder = new FlagHolder();
    flagHolder.reducedFlag = false;
    reduceButton.setOnAction( new EventHandler<ActionEvent>()
    {
      @Override
      public void handle( ActionEvent e )
      {
        if( flagHolder.reducedFlag )
        {
          reduceButton.setText( " Aggregated Task List" );
          populateTable( mainTable, taskReader.getTaskInfoList() );
          flagHolder.reducedFlag = false;
          return;
        }
        reduceButton.setText( "Full Task List" );
        flagHolder.reducedFlag = true;
        populateTable( mainTable, taskReader.getAggregatedTaskInfoList() );
      }
    } );

    exportExcelButton.setOnAction( new EventHandler<ActionEvent>()
    {
      @Override
      public void handle( ActionEvent e )
      {
        File file = getFileChooser( "Excel files (*.xlsx)", "*.xlsx" ).showSaveDialog( stage );
        if( file != null )
        {
          Excel.getInstance().writeReport(flagHolder.reducedFlag ? taskReader.getAggregatedTaskInfoList() : taskReader.getTaskInfoList(), file );
        }
      }
    } );

    exportXmlButton.setOnAction( new EventHandler<ActionEvent>()
    {
      @Override
      public void handle( ActionEvent e )
      {
        File file = getFileChooser( "XML files (*.xml)", "*.xml" ).showSaveDialog( stage );
        if( file != null )
        {
          Xml.getInstance().writeToFile(flagHolder.reducedFlag ? taskReader.getAggregatedTaskInfoList() : taskReader.getTaskInfoList(), file );
        }
      }
    } );

    importXmlButton.setOnAction( new EventHandler<ActionEvent>()
    {
      @Override
      public void handle( ActionEvent e )
      {
        File file = getFileChooser( "XML files (*.xml)", "*.xml" ).showOpenDialog( stage );
        if( file != null )
        {
          List<TaskInfo> taskInfoList = Xml.getInstance().readFromFile( file );
          compareTableData(taskInfoList);
          populateTable(compareTable, taskInfoList );

        }
      }

    } );

  }

  private void initTableColumns( TableView<TaskInfo> table )
  {
    TableColumn<TaskInfo,String> procNameCol = new TableColumn<>( "Process Name" );
    procNameCol.setMinWidth( 100 );
    procNameCol.setCellValueFactory( new PropertyValueFactory<TaskInfo,String>( "processName" ) );

    TableColumn<TaskInfo,String> pidCol = new TableColumn<>( "PID" );
    pidCol.setMinWidth( 100 );
    pidCol.setCellValueFactory( new PropertyValueFactory<TaskInfo,String>( "pid" ) );

    TableColumn<TaskInfo,String> usedMemoryCol = new TableColumn<>( "Used Memory" );
    usedMemoryCol.setMinWidth( 100 );
    usedMemoryCol.setCellValueFactory( new PropertyValueFactory<TaskInfo,String>( "usedMemory" ) );
    table.getColumns().addAll( pidCol, procNameCol, usedMemoryCol );
  }

  private void populateTable( TableView<TaskInfo> table, List<TaskInfo> taskInfoList )
  {
    table.getItems().removeAll();
    ObservableList<TaskInfo> data = FXCollections.observableArrayList( taskInfoList );
    table.setItems( data );
  }

  private FileChooser getFileChooser( String extensionFilterDescription, String extension )
  {
    FileChooser fileChooser = new FileChooser();

    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter( extensionFilterDescription, extension );
    fileChooser.getExtensionFilters().add( extFilter );
    return fileChooser;
  }

  private static class FlagHolder
  {
    public boolean reducedFlag;
  }

  private void compareTableData(List<TaskInfo> taskInfoList)
  {
    ObservableList<TaskInfo> mainTableContent = mainTable.getItems();
    int limit =  taskInfoList.size() <= mainTableContent.size() ? taskInfoList.size() : mainTableContent.size();

    for( int i= 0; i < limit; i++ )
    {
      TaskInfo current = taskInfoList.get(i);
      if( !mainTableContent.get(i).equals(current)) {
        current.setProcessName("!!" + current.getProcessName() + "!!");
      }
    }
  }
}
