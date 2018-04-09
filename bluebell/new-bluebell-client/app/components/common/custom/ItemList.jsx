import React, {Component} from 'react';

var tableInstance;

export default class ItemList extends Component {
    constructor(props) {
        super(props);

        this.initializeTableConfig = this.initializeTableConfig.bind(this);
        //this.updateTableConfig = this.updateTableConfig.bind(this);
    }

    componentDidMount() {
        console.log('did mount')
        this.initializeTableConfig();
    }

    componentWillReceiveProps(nextProps){
        console.log('will mount')
        this.updateTableConfig();
    }

    updateTableConfig(){
       // $('.dataTable').load(document.URL + '.dataTable');   
        //this.forceUpdate();
    }

    initializeTableConfig(){
        var that = this;
        //console.log('data is :' + JSON.stringify(that.props.data))
        $('document').ready(()=>{
            if(this.props.showActionButtons){
                    tableInstance =  $('.dataTable').DataTable(
                    {
                        pageLength: 20,
                        responsive: true,
                        searching: false,
                        dom: '<"html5"B>frtip',
                        "bProcessing": true,
                        "aaData": that.props.data,
                        "aoColumns": that.props.columnMetadata,
                        "aoColumnDefs": [
                            {
                            "aTargets": [-1],
                            "bSortable": false,
                            "mRender": function(data, type, full) {
                                return that.props.actionButtons;
                            }
                        }]
                    });
                    $('.dataTable tbody').on('click', that.props.editId, function () {
                        var rowData = tableInstance.row($(this).parents("tr")).data();
                        that.props.handleEdit(rowData);
                    });
                    $('.dataTable tbody').on('click', that.props.deleteId, function () {
                        var rowData = tableInstance.row($(this).parents("tr")).data();
                        that.props.handleDelete(rowData.clientId);
                    });
            }else{
                tableInstance =  $('.dataTable').DataTable({
                        pageLength: 20,
                        responsive: true,
                        searching: false,
                        dom: '<"html5"B>frtip',
                        "bProcessing": true,
                        "aaData": that.props.data,
                        "aoColumns": that.props.columnMetadata,
                });
            }
        });
        
    }
   
    render() {
        console.log('inside itemlist')
        return (
         
                    <div className="table-responsive">
                        <table className="table table-striped table-bordered table-hover dataTable" > 
                            <thead>
                                <tr>
                                    {this.props.heading.map((heading,key) =>{
                                        return(
                                            <th key={key}>{heading}</th>
                                        )
                                    })}
                                </tr>
                            </thead>
                        </table>
                    </div>
                   
        )
    }
}