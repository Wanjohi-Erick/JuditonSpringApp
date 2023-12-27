var idcol;
var id;
var Myurl;
var actionurl;
var rowindex;
var viewtitle;
var tablebeingedited;
var maintable;
var searchitems = [];
var searchitems2 = [];
var dataarray;
var rows_selected = [];
var rows_selected2 = [];
var searchedphones = [];
var img;
var schoollogo;
var myform;
var schoollogo;
var modaltoopen;
var uniqueid;
var actionsaveorupdate;
var uploadimagesoption;
var examid;
var collumns;


var subname;


var theme;

$(document).ready(function () {

    $('#daterangepickr').daterangepicker();
    $('#daterangepickrteachers').daterangepicker();


    $('#yesBtn').click(function (e) {

        link = $(this);

        dynamicdeleteitem(id, tablebeingedited, link.attr("href"));


        e.preventDefault();
        $('#confirmModal').modal('hide');
    });

});


function base64ToArrayBuffer(base64) {
    var binaryString = window.atob(base64);
    var binaryLen = binaryString.length;
    var bytes = new Uint8Array(binaryLen);
    for (var i = 0; i < binaryLen; i++) {
        var ascii = binaryString.charCodeAt(i);
        bytes[i] = ascii;
    }
    return bytes;
}


const createdCell = function (cell) {
    let original;
    cell.setAttribute('contenteditable', true)
    cell.setAttribute('spellcheck', false)
    cell.addEventListener("focus", function (e) {
        original = e.target.textContent
    })
    cell.addEventListener("blur", function (e) {
        if (original !== e.target.textContent) {
            const row = maintable.row(e.target.parentElement)
            row.invalidate()
        }
    })
}


function dynamictable(data, Mydiv, url, tablee, tabletoasignnewrow, Modal, Form) {

    $(tablee).DataTable().destroy(true);

    if (url != "getsearchaccounts" && url != "getpreviousreceipts" && url != "getsearchstudentsforsplit") {
        searchitems.length = 0;
    }

    if (url != 'postdatagetschools') {
        rows_selected.length = 0;
    }


    rows_selected2.length = 0;

    tablebeingedited = tabletoasignnewrow;
    maintable = tablee;
    dataarray = data;
    myform = Form;
    modaltoopen = Modal;


    collumns = Object.keys(data[0]);

    Myurl = url;

    $(Mydiv).empty();


    var html = '<table  id=' + tablee.substring(1) + ' class="table table-hover" style="font-size:12px">';

    html += '<thead style="" class="text-primary">';

    html += '<tr>';


    if (url != 'postexameditmarksforreg' && url != 'getfeebalances' && url != 'feestructure' && url != 'getfeeinvoicing' &&
        url != 'posttogetperformanceperstream' && url != 'postgetsubjectselection' && url != 'posttogetmarksasadmin') {
        html += '<td style="width:5px"> <div class="checkbox tiny"><div class="checkbox-container"><input id="select_all" value="1" type="checkbox" /><div class="checkbox-checkmark"></div></div></div> </td>'
    }

    for (i = 0; i < collumns.length; i++) {

        if (collumns[i] == "id") {
            html += '<td class="id_event">' + collumns[i] + '</td>';
        } else if (collumns[i] == "action") {
            html += '<td class="text-center">' + collumns[i] + '</td>';
        } else if (collumns[i] == "Sms") {
            html += '<td style="width: 60%">' + collumns[i] + '</td>';
        } else if (collumns[i].includes("mceesub:: ")) {
            subname = collumns[i].substring(10);
            html += '<td class="text-left">' + collumns[i].substring(10) + '</td>';
        } else {

            if (url == 'posttogetmarksasadmin' && collumns[i] != "Name" && collumns[i] != "Adm no" && collumns[i] != "Id") {
                html += '<td class="text-center">' + collumns[i] + '</td>';
            } else {
                html += '<td>' + collumns[i] + '</td>';
            }

        }

    }


    html += '</tr>';

    html += '</thead>';

    html += '<tbody>';


    var dtasize = data.length;

    if (url == "poststudentdataforreg") {

        viewtitle = "View student  profile";

        if (data.length > 50) {
            dtasize = 50;
        } else {
            dtasize = data.length;
        }

    } else if (url == "getsearchstudents" || url == "getsearchaccounts") {
        dtasize = 0;
    }

    for (var i = 0; i < dtasize; i++) {


        html += '<tr>';

        if (url != 'postexameditmarksforreg' && url != 'feestructure' && url != 'getfeebalances' && url != 'getfeeinvoicing'
            && url != 'posttogetperformanceperstream' && url != 'postgetsubjectselection' && url != 'posttogetmarksasadmin') {
            html += '<td></td>'
        }

        var object = data[i];


        for (var property in object) {

            if (property == "id") {
                html += '<td class="id_event">' + object[property] + '</td>';
            } else if (property == "action" || property == "Action") {
                if (url == 'postclasstogetfeestructure') {
                    html += "<td class='project-actions text-center'>  <a id='btnDelete'  class='btn btn-danger btn-sm' href='#'><i class='fas fa-trash'></i></a>    </td>";
                } else if (url == 'postgetotherincomes' || url == 'postgetpaymentvouchers') {
                    html += "<td class='project-actions text-center'>    <a id='btnPrint' class='btn btn-primary btn-sm' href='#' title='" + viewtitle + "'><i class='fas fa-print'></i></i></a>     <a  id='btnEdit'  style='margin:5px' class='btn btn-info btn-sm' href='#'><i class='fas fa-pencil-alt'></i> </a>     <a id='btnDelete'  class='btn btn-danger btn-sm' href='#'><i class='fas fa-trash'></i></a>    </td>";
                } else if (Myurl == 'feerefunds') {
                    html += "<td><div><button type='button' class='btn btn-danger dropdown-toggle' data-toggle='dropdown'><span class='caret'></span><span class='sr-only'>Toggle Dropdown</span></button> <ul class='dropdown-menu' role='menu'>  <li><a id='btnApprove' href='#'>Approve</a></li> <li><a id='btnReject' href='#'>Reject</a></li> <li><a id='btnDelete' href='#'>Delete</a></li> <li><a id='btnpay' href='#'>Make payment</a></li></ul></div></td>"
                } else if (url == 'getpreviousreceipts') {
                    html += "<td class='project-actions text-center'>    <a id='btnPrintreceipt' class='btn btn-primary btn-sm' href='#' title='" + viewtitle + "'><i class='fas fa-print'></i></i></a>     <a  id='btncross' style='font-size:12px' class='btn btn-info btn-danger' href='#'><i class='fas fa-remove'></i> </a>     <a id='btnrestore'  class='btn btn-success btn-sm' href='#'><i class='fas fa-check'></i></a>    </td>";
                } else if (url == 'getcurrentterms' || url == 'financialyears') {
                    html += "<td class='project-actions text-center'>        <a  id='btnEdit'  style='margin:5px' class='btn btn-info btn-sm' href='#'><i class='fas fa-pencil-alt'></i> </a>     <a id='btnDelete'  class='btn btn-danger btn-sm' href='#'><i class='fas fa-trash'></i></a>    </td>";
                } else if (url == 'receipts' || url == 'feecollecton' || url == 'groupreceipts') {
                    html += "<td class='project-actions text-center'>        <a id='btnPrint' class='btn btn-primary btn-sm' href='#' title='" + viewtitle + "'><i class='fas fa-print'></i></i></a> </td>";
                } else if (url == 'feestructure' || url == 'getsearchstudentsforsplit') {
                    html += "<td class='project-actions text-center'>    <a id='btnDelete'  class='btn btn-danger btn-sm' href='#'><i class='fas fa-trash'></i></a>    </td>";
                } else {
                    html += "<td class='project-actions text-center'>    <a id='btnView' class='btn btn-primary btn-sm' href='#' title='" + viewtitle + "'><i class='fas fa-eye'></i></i></a>     <a  id='btnEdit'  style='margin:5px' class='btn btn-info btn-sm' href='#'><i class='fas fa-pencil-alt'></i> </a>     <a id='btnDelete'  class='btn btn-danger btn-sm' href='#'><i class='fas fa-trash'></i></a>    </td>";
                }
            } else if (property == "status") {
                html += '<td class="project-state text-left"><span class="badge badge-success">' + object[property] + '</span></td>';
            } else if (property == "profile") {
                html += '<td class="project_progress"><div class="progress progress-sm"><div class="progress-bar bg-green" role="progressbar" aria-valuenow="57" aria-valuemin="0" aria-valuemax="100" style="width: 57%"></div></div><small>' + object[property] + '</small> </td>';
            } else if (property == "simage") {
                html += '<td> <img  style= "width:50px;height:50px; border-radius: 50%; " src=' + object[property] + '> </td>';
            } else {

                html += '<td>' + object[property] + '</td>';

            }

        }

        html += '</tr>';


    }


    html += '</tbody>';

    html += '</table>';


    $(html).appendTo(Mydiv);

    if (url == 'getsearchstudentsforsplit') {
        $(tablee).DataTable().clear();
    }


    if (url == 'feecollecton') {

        $(tablee).DataTable({
            "responsive": true,
            "lengthChange": false,
            "bDestroy": true,
            "aaSorting": [[2, 'desc']],
            "bFilter": false,
            "autoWidth": false,
            pageLength: 7,
            buttons: [{extend: 'copy', footer: false},

                {
                    text: 'Pdf',
                    action: function (e, dt, node, config) {
                        Myurl = 'printfeebcollection';
                        openTestModal();
                    }
                },
                {extend: 'csv', footer: false},
                {extend: 'excel', footer: false}
            ],
            'columnDefs': [{'visible': false, 'targets': [1]}, {
                'targets': 0,
                'searchable': false,
                'orderable': false,
                'width': '1%',
                'className': 'dt-body-center',
                'render': function (data, type, row) {

                    return '<div class="checkbox tiny"><div class="checkbox-container"> <input type="checkbox"/><div class="checkbox-checkmark"></div></div></div>';
                }
            }],
            'rowCallback': function (row, data, index) {

                $(row).find('td:nth-child(10)').css('color', 'green');
                $(row).find('td:nth-child(10)').css('font-weight', 'bold');

                $(row).find('td:nth-child(6)').css('color', 'black');
                $(row).find('td:nth-child(6)').css('font-weight', 'bold');

                $(row).find('td:nth-child(4)').css('color', 'grey');
                $(row).find('td:nth-child(4)').css('font-weight', 'bold');

                $(row).find('td:nth-child(2)').css('color', 'black');
                $(row).find('td:nth-child(2)').css('font-weight', 'bold');

            }

        }).buttons().container().appendTo(tablee + '_wrapper .col-md-6:eq(0)');

    } else if (url == 'receipts') {

        $(tablee).DataTable({
            "responsive": true,
            "lengthChange": false,
            "bDestroy": true,
            "aaSorting": [[2, 'desc']],
            "bFilter": false,
            "autoWidth": false,
            pageLength: 6,
            'columnDefs': [{
                'targets': 0,
                'searchable': false,
                'orderable': false,
                'width': '1%',
                'className': 'dt-body-center',
                'render': function (data, type, row) {

                    return '<div class="checkbox tiny"><div class="checkbox-container"> <input type="checkbox"/><div class="checkbox-checkmark"></div></div></div>';
                }
            }],
            'rowCallback': function (row, data, index) {

                $(row).find('td:nth-child(9)').css('color', 'green');
                $(row).find('td:nth-child(9)').css('font-weight', 'bold');

                $(row).find('td:nth-child(6)').css('color', 'black');
                $(row).find('td:nth-child(6)').css('font-weight', 'bold');

                $(row).find('td:nth-child(4)').css('color', 'grey');
                $(row).find('td:nth-child(4)').css('font-weight', 'bold');

                $(row).find('td:nth-child(2)').css('color', 'black');
                $(row).find('td:nth-child(2)').css('font-weight', 'bold');

            }

        }).buttons().container().appendTo(tablee + '_wrapper .col-md-6:eq(0)');

    } else {

        $(tablee).DataTable({
            "responsive": true,
            "lengthChange": false,
            "bDestroy": true,
            "aaSorting": [[2, 'desc']],
            "autoWidth": false,
            pageLength: 7,
            'columnDefs': [{'visible': false, 'targets': [1, 2, 3]}, {
                'targets': 0,
                'searchable': false,
                'orderable': false,
                'width': '1%',
                'className': 'dt-body-center',
                'render': function (data, type, row) {
                    if (row[2] == '1') {
                        var rowId = row[3];
                        var index = $.inArray(rowId, rows_selected);
                        if (index === -1) {
                            rows_selected.push(rowId);
                        }
                    }
                    return '<div class="checkbox tiny"><div class="checkbox-container"><input type="checkbox"  ' + ((row[2] == '1') ? 'checked' : '') + '  /><div class="checkbox-checkmark"></div></div></div>';
                }
            }]
        }).buttons().container().appendTo(tablee + '_wrapper .col-md-6:eq(0)');

    }


    $(tablee + ' tbody').on('click', 'input[type="checkbox"]', function (e) {

        var $row = $(this).closest('tr');

        // Get row data
        var data = $(tablee).DataTable().row($row).data();

        rowId = data[2];

        // Determine whether row ID is in the list of selected row IDs
        var index = $.inArray(rowId, rows_selected);

        // If checkbox is checked and row ID is not in list of selected row IDs
        if (this.checked && index === -1) {
            rows_selected.push(rowId);

            // Otherwise, if checkbox is not checked and row ID is in list of selected row IDs
        } else if (!this.checked && index !== -1) {
            rows_selected.splice(index, 1);
        }

        $("#totalstudenstobebilled").html(rows_selected.length + "  Students Selected");


        if (this.checked) {
            $row.addClass('selected');
        } else {
            $row.removeClass('selected');
        }

        updateDataTableSelectAllCtrl($(tablee).DataTable());

        // Prevent click event from propagating to parent
        e.stopPropagation();
    });


    var table = $(tablee).DataTable();
    $('#select_all').click(function (e) {

        var chk = $(this).prop('checked');
        var rowcollection = table.$(".call-checkbox", {"page": "all"});
        var i = 0;
        rowcollection.each(function (index, elem) {
            //You have access to the current iterating row
            var checkbox_value = $(elem).val();
            $(this).prop('checked', chk);

            var $row = $(this).closest('tr');

            // Get row data
            var data = $(tablee).DataTable().row($row).data();

            rowId = data[2];

            // Determine whether row ID is in the list of selected row IDs
            var index = $.inArray(rowId, rows_selected);

            // If checkbox is checked and row ID is not in list of selected row IDs
            if (this.checked && index === -1) {
                rows_selected.push(rowId);

                // Otherwise, if checkbox is not checked and row ID is in list of selected row IDs
            } else if (!this.checked && index !== -1) {
                rows_selected.splice(index, 1);
            }


            i++;
            //Do something with 'checkbox_value'
        });

        $("#totalstudenstobebilled").html(rows_selected.length + "  Students Selected");

    });


    $(tablee).on('click', '#btnEdit', function () {

        rowindex = $(this).closest('tr').index();
        var table = $(tablee).DataTable();
        var tr = $(this.closest('tr'));
        if (tr.hasClass('child')) {
            tr = tr.prev();
        }
        rowindex = table.row(tr).index();
        var data = table.row(tr).data();
        data.forEach(function (d, index, arr) {
            d = $('<div>').html(d);
            arr[index] = d.val() || d.text()
        })


        id = data[2];


        if (url == 'getaccountgroups') {
            dynamicformsubmit(Form, "/api/finance/getaccountgroup", id, 'Londonstudents', Modal);
        }


    });


    $(tablee + ' tbody').on('click', '#btnDelete', function () {
        var table = $(tablee).DataTable();
        var tr = $(this.closest('tr'));
        if (tr.hasClass('child')) {
            tr = tr.prev();
        }

        rowindex = table.row(tr).index();
        var data = table.row(tr).data();
        data.forEach(function (d, index, arr) {
            d = $('<div>').html(d);
            arr[index] = d.val() || d.text()
        })


        id = data[2];

        $('#myModaldelete').modal({
            backdrop: 'static',
            keyboard: false
        });
    });


    $(tablee + ' tbody').on('click', '#btnPrint', function () {
        var table = $(tablee).DataTable();
        var tr = $(this.closest('tr'));
        if (tr.hasClass('child')) {
            tr = tr.prev();
        }

        rowindex = table.row(tr).index();
        var data = table.row(tr).data();
        data.forEach(function (d, index, arr) {
            d = $('<div>').html(d);
            arr[index] = d.val() || d.text()
        })


        if (url == 'groupreceipts') {
            id = data[3];
            Myurl = 'printgroupreceipts';
            openTestModal();
        }


    });


    $(tablee + ' tbody').on('click', '#btnPrintreceipt', function () {
        var table = $(tablee).DataTable();
        var tr = $(this.closest('tr'));
        if (tr.hasClass('child')) {
            tr = tr.prev();
        }

        rowindex = table.row(tr).index();
        var data = table.row(tr).data();
        data.forEach(function (d, index, arr) {
            d = $('<div>').html(d);
            arr[index] = d.val() || d.text()
        })


        id = data[4];

        Myurl = 'Printfeereceipt';
        openTestModal();

    });


    $(tablee + ' tbody').on('click', '#btnApprove', function () {
        var table = $(tablee).DataTable();
        var tr = $(this.closest('tr'));
        if (tr.hasClass('child')) {
            tr = tr.prev();
        }

        rowindex = table.row(tr).index();
        var data = table.row(tr).data();
        data.forEach(function (d, index, arr) {
            d = $('<div>').html(d);
            arr[index] = d.val() || d.text()
        })


        id = data[2];

        if (url == 'feerefunds') {
            Myurl = 'approverefund';
            openTestModal();
        }

    });


    $(tablee + ' tbody').on('click', '#btnReject', function () {
        var table = $(tablee).DataTable();
        var tr = $(this.closest('tr'));
        if (tr.hasClass('child')) {
            tr = tr.prev();
        }

        rowindex = table.row(tr).index();
        var data = table.row(tr).data();
        data.forEach(function (d, index, arr) {
            d = $('<div>').html(d);
            arr[index] = d.val() || d.text()
        })


        id = data[2];

        if (url == 'feerefunds') {
            Myurl = 'rejectrefund';
            openTestModal();
        }

    });


    $(tablee + ' tbody').on('click', '#btnpay', function () {
        var table = $(tablee).DataTable();
        var tr = $(this.closest('tr'));
        if (tr.hasClass('child')) {
            tr = tr.prev();
        }

        rowindex = table.row(tr).index();
        var data = table.row(tr).data();
        data.forEach(function (d, index, arr) {
            d = $('<div>').html(d);
            arr[index] = d.val() || d.text()
        })


        id = data[2];

        if (url == 'feerefunds') {
            $('#popupayfeerefund').modal({
                backdrop: 'static',
                keyboard: false
            });
        }

    });


    $(tablee + ' tbody').on('click', '#btncross', function () {
        var table = $(tablee).DataTable();
        var tr = $(this.closest('tr'));
        if (tr.hasClass('child')) {
            tr = tr.prev();
        }

        rowindex = table.row(tr).index();
        var data = table.row(tr).data();
        data.forEach(function (d, index, arr) {
            d = $('<div>').html(d);
            arr[index] = d.val() || d.text()
        })


        id = data[4];


        $('#popupcrossreceipt').modal({
            backdrop: 'static',
            keyboard: false
        });


    });


    $(tablee + ' tbody').on('click', '#btnrestore', function () {
        var table = $(tablee).DataTable();
        var tr = $(this.closest('tr'));
        if (tr.hasClass('child')) {
            tr = tr.prev();
        }

        rowindex = table.row(tr).index();
        var data = table.row(tr).data();
        data.forEach(function (d, index, arr) {
            d = $('<div>').html(d);
            arr[index] = d.val() || d.text()
        })


        id = data[4];
        Myurl = 'restorereceipt';
        openTestModal();


    });


    $(tablee).on('click', '#btnremovestudentfromtableonly', function () {
        var table = $(tablee).DataTable();
        var row = $(this).parents('tr');

        if ($(row).hasClass('child')) {
            table.row($(row).prev('tr')).remove().draw();
        } else {
            table
                .row($(this).parents('tr'))
                .remove()
                .draw();
        }

        var totalAmount = 0;
        table.rows().every(function (rowIdx, tableLoop, rowLoop) {
            var Row = this.data();
            if (this.cell(rowIdx, 5).nodes().to$().find('input').val()) {
                totalAmount += parseFloat(this.cell(rowIdx, 5).nodes().to$().find('input').val());
            }
        });

        $("#splittedamount").html(numberWithCommas(totalAmount));


    });


}


function getitem(id) {
    dynamicformsubmit("#reasonform", "/api/bio/getReason/", id, 'Londonstudents', "#reasonmodal");
}


function fetchdynamicdata(actionurl, indexed_array, Modal) {


    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: actionurl,
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (result) {


            $("#example2").DataTable().destroy(true);
            dynamictable(result, '#div1entermarks', 'postexameditmarksforreg', '#example2', '#example2');

            $(Modal).modal({
                backdrop: 'static',
                keyboard: false
            });

            Myurl = 'postexammarksforreg'


        },
        error: function (e) {
            //console.log("ERROR: ", e);
        }
    });

}


function updateDataTableSelectAllCtrl(table) {

    var $table = table.table().node();
    var $chkbox_all = $('tbody input[type="checkbox"]', $table);
    var $chkbox_checked = $('tbody input[type="checkbox"]:checked', $table);
    var chkbox_select_all = $('thead input[id="select_all"]', $table).get(0);

    // If none of the checkboxes are checked
    if ($chkbox_checked.length === 0) {
        chkbox_select_all.checked = false;
        if ('indeterminate' in chkbox_select_all) {
            chkbox_select_all.indeterminate = false;
        }

        // If all of the checkboxes are checked
    } else if ($chkbox_checked.length === $chkbox_all.length) {
        chkbox_select_all.checked = true;
        if ('indeterminate' in chkbox_select_all) {
            chkbox_select_all.indeterminate = false;
        }

        // If some of the checkboxes are checked
    } else {
        chkbox_select_all.checked = false;
        if ('indeterminate' in chkbox_select_all) {
            chkbox_select_all.indeterminate = true;
        }
    }
}


$("#searchstudentsfeepaymentsplit").autocomplete({
    source: function (request, response) {
        var results = $.ui.autocomplete.filter(searchitems, request.term);
        response(results.slice(0, 10));
    },
    minLength: 1,
    appendTo: "#popupfeepayment",
    select: function (event, ui) {

        $("#searchstudentsfeepaymentsplit").val((ui.item.value))

        Myurl = "fetchstudentdetailsforsolit";
        openTestModal();

    }

});
$("#searchstudentsfeepaymentsplit").autocomplete("widget").attr('style', 'max-height: 400px;max-width: 285px;font-size:12px;overflow-y: auto; overflow-x: hidden;')


var reportTitle;
var listofstudentsautocomplete;
var extrareportpath;


function openTestModal() {
    $('#modalprogress2').modal({
        keyboard: false,
        backdrop: 'static'
    });
}


$('#modalprogress2').on('shown.bs.modal', function (e) {

    if (Myurl == 'savetransfercash') {

        var indexed_array = {};

        indexed_array['fromaccount'] = id;
        indexed_array['toaccount'] = $('#transfercashtoaccount').val();
        indexed_array['date'] = $('#transfercashdate').val();
        indexed_array['amount'] = $('#transfercashamount').val();
        indexed_array['transnu'] = $('#transfercashtransactionnu').val();

        $.ajax({

            type: "POST",
            url: '/api/finance/posttransfercash',
            contentType: "application/json",
            data: JSON.stringify(indexed_array),
            success: function (data) {

                var object = JSON.parse(data)[0];

                $("#modalprogress2").modal("hide");
                $("#popuptransfercash").modal("hide");

                $(function () {
                    $("#messageid").html(object['querystatus']);
                    $('#myModal').modal('show');
                    setTimeout(function () {
                        $('#myModal').modal('hide');
                    }, 2000);
                });

            }
        });


    } else if (Myurl == 'printincomestatement') {


        var indexed_array = {};
        indexed_array['group'] = $('#printincomegroup').val();
        indexed_array['daterange'] = new Date($('#printincomedaterange').val().split(' - ')[0] + ' GMT').toISOString().split('T')[0] + " - " + new Date($('#printincomedaterange').val().split(' - ')[1] + ' GMT').toISOString().split('T')[0];

        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: "/api/finance/incomestatement",
            data: JSON.stringify(indexed_array),
            success: function (result) {

                $("#modalprogress2").modal("hide");
                var sampleArr = base64ToArrayBuffer(result);
                var file = new Blob([sampleArr], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);


            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#modalprogress2").modal("hide");
            }
        });

    }


});


function opendefaultmodal() {

    actionsaveorupdate = 'Save';


    if (Myurl == 'postgetotherincomes') {

        $.ajax({
            url: '/api/finance/getvotes',
            contentType: "application/json",
            success: function (data) {

                $("#example2").DataTable().destroy(true);

                searchitems.length = 0;

                listofstudentsautocomplete = JSON.parse(data);
                for (var i = 0; i < listofstudentsautocomplete.length; i++) {
                    searchitems.push(listofstudentsautocomplete[i].id + " " + listofstudentsautocomplete[i].Account);
                }
                listofstudentsautocomplete = JSON.parse(data);
                dynamictable(listofstudentsautocomplete, '#searchaccountdiv', 'getsearchaccounts', '#example2', '#example2');


                $.ajax({
                    url: '/api/finance/getlistofstudents',
                    contentType: "application/json",
                    success: function (data) {


                        searchitems2.length = 0;

                        listofstudents = JSON.parse(data);
                        for (var i = 0; i < listofstudents.length; i++) {
                            searchitems2.push(listofstudents[i].Name);
                        }


                        $('#popupotherincomes').modal({
                            backdrop: 'static',
                            keyboard: false
                        });

                        $("#saveotherincomesbutt").html("Save");
                    }

                });


            }

        });

    } else {

        $(myform)[0].reset();
        $(modaltoopen).modal({
            backdrop: 'static',
            keyboard: false
        });

        $("#formsubmitbuttaccgroup").html("Save");
        $("#formsubmitbuttcurrentterms").html("Save");
        $("#formsubmitbuttfinancialyears").html("Save");


    }

}


$('#clockinsmsoptiondontsend').click(function () {


    var indexed_array = {};

    indexed_array['value'] = 0;

    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: '/api/bio/clockinsmsoptiondontsend',
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (result) {


        }
    });
});


$("#endsendingtime").keypress(function (e) {
    if (e.which == 13) {
        var indexed_array = {};

        indexed_array['value'] = $("#endsendingtime").val();

        $('#buttonnotifications').click();

        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: '/api/bio/updateendsendingtime',
            data: JSON.stringify(indexed_array),
            dataType: 'json',
            success: function (result) {

            }
        });

    }
});


$("#startsendingtime").keypress(function (e) {
    if (e.which == 13) {
        var indexed_array = {};

        indexed_array['value'] = $("#startsendingtime").val();

        $('#buttonnotifications').click();

        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: '/api/bio/updatestartsendingtime',
            data: JSON.stringify(indexed_array),
            dataType: 'json',
            success: function (result) {

            }
        });

    }
});


$('#clockinsmsoption').click(function () {


    var indexed_array = {};

    indexed_array['value'] = 1;

    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: '/api/bio/updateclockinsmsoption',
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (result) {


        }
    });
});


$('#sendsmsforstaff').click(function () {

    var indexed_array = {};

    if ($('#sendsmsforstaff').is(":checked")) {
        $('#dontsendsmsforstaff').prop('checked', false);
        indexed_array['value'] = 0;
    } else {
        $('#dontsendsmsforstaff').prop('checked', true);
        indexed_array['value'] = 1;
    }

    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: '/api/bio/updatesendsmsforstaff',
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (result) {


        }
    });


});


$('#dontsendsmsforstaff').click(function () {

    var indexed_array = {};

    if ($('#dontsendsmsforstaff').is(":checked")) {
        $('#sendsmsforstaff').prop('checked', false);
        indexed_array['value'] = 1;
    } else {
        $('#sendsmsforstaff').prop('checked', true);
        indexed_array['value'] = 0;
    }

    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: '/api/bio/updatedontsendsmsforstaff',
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (result) {


        }
    });


});


function populate(frm, data, Myurl) {

    $(frm)[0].reset();

    $.each(data, function (key, value) {
        var ctrl = $('[name="' + key + '"]', frm);
        switch (ctrl.prop("type")) {
            case "radio":
            case "checkbox":
                ctrl.each(function () {
                    if ($(this).attr('value') == value) $(this).attr("checked", value);
                });
                break;
            case "select-one":
                var id = ctrl.attr("id");
                if (id == 'Serving') {
                    Servingselect.setChoiceByValue(value);
                } else if (id == 'Category') {
                    Categorygadgetselect.setChoiceByValue(value);
                }
                break;
            default:
                ctrl.val(value);


                if (Myurl == 'postexamdataforreg' && key == 'customexamname' && value != '') {
                    $('#customexamnamediv').show();
                    $('#customexamnamecheck').prop('checked', true);

                    $('#includeallclassescheck').prop('checked', false);
                    $('#includeallclassescheck').attr("disabled", true);

                }
                if (Myurl == 'postexamdataforreg' && value == '') {
                    $('#customexamnamecheck').prop('checked', false);
                    $('#includeallclassescheck').attr("disabled", true);
                }

        }

        if (key == 'simage') {
            $("#simage" + uploadimagesoption).attr("src", imgname = value);
        }

    });


}


function dynamicdeleteitem(id, tablebeingedited, Myurl) {

    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: Myurl,
        dataType: 'json',
        success: function (result) {
            window.location.reload();
        },
        error: function (e) {
            //console.log("ERROR: ", e);
        }
    });


}


function dynamicformsubmit(myform, actionurl, id, action, Modal) {

    actionsaveorupdate = 'Save Changes';

    var indexed_array = {};
    indexed_array['id'] = id;


    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: actionurl + id,
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (result) {

            populate(myform, result, Myurl);


        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });


}


function getgadget(id) {
    dynamicformsubmit("#gadgetsform", "/api/bio/getgadget/", id, 'Londonstudents', "#gadgetsmodal");
}


function printpresentorapresent(id, type, clsvalue) {
    var indexed_array = {};
    indexed_array['class'] = id;
    indexed_array['type'] = type;
    indexed_array['classvalue'] = clsvalue;


    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: '/api/bio/printabsentorpresentperclass',
        data: JSON.stringify(indexed_array),
        success: function (result) {


            var sampleArr = base64ToArrayBuffer(result);
            var file = new Blob([sampleArr], {type: 'application/pdf'});
            var fileURL = URL.createObjectURL(file);
            window.open(fileURL);

            return;


        },
        error: function (e) {
            //console.log("ERROR: ", e);
        }
    });

}


function submitFormreports(event, myform) {

    event.preventDefault();


    var unindexed_array = $('#' + myform).serializeArray();
    var indexed_array = {};


    if (myform == 'studentsclockform') {
        actionurl = "/api/bio/getstudentsreport";
        indexed_array['classtext'] = $("#class option:selected").text();
        indexed_array['daterange'] = new Date($('#daterangepickr').val().split(' - ')[0] + ' GMT').toISOString().split('T')[0] + " - " + new Date($('#daterangepickr').val().split(' - ')[1] + ' GMT').toISOString().split('T')[0];
    } else if (myform == 'teachersclockform') {
        actionurl = "/api/bio/getteacersreport";
        indexed_array['daterange'] = new Date($('#daterangepickrteachers').val().split(' - ')[0] + ' GMT').toISOString().split('T')[0] + " - " + new Date($('#daterangepickrteachers').val().split(' - ')[1] + ' GMT').toISOString().split('T')[0];
    }


    $.map(unindexed_array, function (n, i) {
        indexed_array[n['name']] = n['value'];
    });


    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: actionurl,
        data: JSON.stringify(indexed_array),
        success: function (result) {


            var sampleArr = base64ToArrayBuffer(result);
            var file = new Blob([sampleArr], {type: 'application/pdf'});
            var fileURL = URL.createObjectURL(file);
            window.open(fileURL);

            return;


        },
        error: function (e) {
            //console.log("ERROR: ", e);
        }
    });


};


function submitForm(event, myform) {

    event.preventDefault();


    var unindexed_array = $('#' + myform).serializeArray();
    var indexed_array = {};


    if (myform == 'gadgetsform') {
        actionurl = "/api/bio/addgadget";
    }


    $.map(unindexed_array, function (n, i) {
        indexed_array[n['name']] = n['value'];
    });

    console.log(indexed_array);

    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: actionurl,
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (result) {


            var object = result[0];
            $(function () {
                $(modaltoopen).modal('hide');
                $("#messageid").html(object['querystatus']);
                $('#myModal').modal('show');
                setTimeout(function () {
                    $('#myModal').modal('hide');
                }, 1000);
            });


            if (myform == 'employeeallowanceform') {

                getemployeepayslip(payno);
                getemployeesdata();
                $('#modalemployeeearning').modal('hide');
            } else {

                window.location.reload();

            }


        },
        error: function (e) {
            //console.log("ERROR: ", e);
        }
    });


};


function toDataURL(url, callback) {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        var reader = new FileReader();
        reader.onloadend = function () {
            callback(reader.result);
        }
        reader.readAsDataURL(xhr.response);
    };
    xhr.open('GET', url);
    xhr.responseType = 'blob';
    xhr.send();
}


Date.prototype.toDateInputValue = (function () {
    var local = new Date(this);
    local.setMinutes(this.getMinutes() - this.getTimezoneOffset());
    return local.toJSON().slice(0, 10);
});

