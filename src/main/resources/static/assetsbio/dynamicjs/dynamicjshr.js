var idcol;
var id;
var payno;
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


function dynamictable(data, Mydiv, url, tablee, tabletoasignnewrow, Modal, Form) {

    $(tablee).DataTable().destroy(true);

    if (url != "getsearchaccounts" && url != "getpreviousreceipts") {
        searchitems.length = 0;
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


    var html = '<table  id=' + tablee.substring(1) + ' class="table table-flush">';

    html += '<thead style="" class="thead-light">';

    html += '<tr>';


    for (i = 0; i < collumns.length; i++) {
        if (collumns[i] == "id" || collumns[i] == "Id") {
            html += '<th style="display:none;"></th>'
        } else {
            html += '<th>' + collumns[i] + '</th>';
        }
    }

    html += '</tr>';

    html += '</thead>';


    var allEmpty = Object.keys(data[0]).every(function (key) {
        return data[0][key].length === 0
    })


    if (!allEmpty) {


        html += '<tbody>';


        var dtasize = data.length;


        for (var i = 0; i < dtasize; i++) {


            html += '<tr>';


            var object = data[i];


            for (var property in object) {


                if (property == "id") {
                    html += '<td style="display:none;" class="id_event">' + object[property] + '</td>';
                } else if ((property == "action" || property == "Action") && object['id'] != "") {
                    if (url == 'individualemployeeallowances' || url == 'individualemployeedeductionss') {
                        html += "<td class='project-actions text-center'> <a  id='btnEdit'  style='margin:5px' class='btn btn-info btn-sm' href='#' title='Edit'> <i class='material-icons text-white position-relative text-md fs-1'>edit</i></a> 	<a id='btnDelete'  class='btn btn-danger btn-sm mt-3' href='#' title='Delete'> <i class='material-icons text-white position-relative text-md fs-4'>delete</i> </a>  <a id='btnApprove'  class='btn btn-success btn-sm mt-3' href='#' title='Approve'> <i class='material-icons text-white position-relative text-md fs-4'>done</i> </a> <a id='btnReject'  class='btn btn-dark btn-sm mt-3' href='#' title='Decline'> <i class='material-icons text-white position-relative text-md fs-4'>cancel</i> </a> </td>";
                    } else if (url == 'payrolldata') {
                        html += "<td class='align-middle'> <button class='btn btn-link text-secondary mb-0' id='navbarDropdownMenuLink' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='false'><i class='fa fa-ellipsis-v text-xs'></i></button>  <div class='dropdown-menu dropdown-menu-end me-sm-n4 me-n3' aria-labelledby='navbarDropdownMenuLink'><a  id='btnhorizonttalpayslip' class='dropdown-item' href='javascript:;'>Horizontal payslip</a> <a id='btnverticalpayslip' class='dropdown-item' href='javascript:;'>Vertical payslip</a> <a  class='dropdown-item' href='javascript:;'>Print P9.a</a> </div></td>"
                    } else if (url == 'payrollpayments') {
                        html += "<td class='project-actions text-center'> <a id='btnDelete'  class='btn btn-danger btn-sm mt-3' href='#' title='Delete'> <i class='material-icons text-white position-relative text-md fs-4'>delete</i> </a>  </td>";
                    } else {
                        html += "<td class='project-actions text-center'> <a  id='btnEdit'  style='margin:5px' class='btn btn-info btn-sm' href='#' data-bs-target='#modalemployeeearning' data-bs-toggle='modal'> <i class='material-icons text-white position-relative text-md fs-1'>edit</i></a> 	<a id='btnDelete'  class='btn btn-danger btn-sm mt-3' href='#'> <i class='material-icons text-white position-relative text-md fs-4'>delete</i> </a>    </td>";
                    }
                } else if (property == "Status" && (object[property] == "Active" || object[property] == "Approved")) {
                    html += '<td class="text-xs font-weight-normal"><div class="d-flex align-items-center"><button class="btn btn-icon-only btn-rounded btn-outline-success mb-0 me-2 btn-sm d-flex align-items-center justify-content-center"><i class="material-icons text-sm" aria-hidden="true">done</i></button><span>' + object[property] + '</span></div></td>';
                } else if (property == "Status" && (object[property] == "Terminated" || object[property] == "Declined")) {
                    html += '<td class="text-xs font-weight-normal"><div class="d-flex align-items-center"><button class="btn btn-icon-only btn-rounded btn-outline-danger mb-0 me-2 btn-sm d-flex align-items-center justify-content-center"><i class="material-icons text-sm" aria-hidden="true">clear</i></button><span>' + object[property] + '</span></div></td>';
                } else if (property == "Status" && (object[property] == "On leave" || object[property] == "Pending Approval")) {
                    html += '<td class="text-xs font-weight-normal"><div class="d-flex align-items-center"><button class="btn btn-icon-only btn-rounded btn-outline-dark mb-0 me-2 btn-sm d-flex align-items-center justify-content-center"><i class="material-icons text-sm" aria-hidden="true">refresh</i></button><span>' + object[property] + '</span></div></td>';
                } else if (property == "Image" && url != 'listofemployees') {
                    html += '<td class="text-xs font-weight-normal"> <img  style= "width:50px;height:50px; border-radius: 50%; " src=' + object[property] + '> </td>';
                } else if (property == "Employee") {
                    html += '<td class="text-xs font-weight-normal"><div class="d-flex align-items-center"><img src=' + object["Image"] + ' class="avatar avatar-xs me-2" alt="user image"><span>' + object["Employee"] + '</span></div></td>';
                } else if (property == "Readings") {
                    html += '<td> <input style="max-width:85px;text-align: center" type="number" value=' + object[property] + '></td>';
                } else {

                    html += '<td class="text-xs font-weight-normal">' + object[property] + '</td>';

                }

            }

            html += '</tr>';


        }


        html += '</tbody>';

    }


    html += '</table>';


    $(html).appendTo(Mydiv);


    if (url == 'individualemployeeallowances' || url == 'individualemployeedeductionss') {

        $(tablee).DataTable({
            "responsive": true,
            "bLengthChange": false,
            "bFilter": false,
            "bInfo": false,
            "autoWidth": false,
            "bPaginate": false,
            pageLength: 7,
            'columnDefs': [{'visible': false, 'targets': [0]}, {
                'render': function (data, type, row) {
                    return '';
                }
            }]
        });

        const dataTableSearch = new simpleDatatables.DataTable(tablee, {
            searchable: false,
            paging: false,
            "responsive": true
        });

    } else if (url == 'payrolldata') {


        $(tablee).DataTable({
            "responsive": true,
            "bLengthChange": false,
            "bFilter": false,
            "bInfo": false,
            "autoWidth": false,
            "bPaginate": false,
            pageLength: 7,
            'columnDefs': [{'visible': false, 'targets': [0]}, {
                'render': function (data, type, row) {
                    return '';
                }
            }]
        });

        new simpleDatatables.DataTable(tablee, {
            searchable: true,
            paging: false,
            "responsive": true
        });

    } else if (url == 'payrollpayments') {


        $(tablee).DataTable({
            "responsive": true,
            "bLengthChange": false,
            "bFilter": false,
            "bInfo": false,
            "autoWidth": false,
            "bPaginate": false,
            pageLength: 7,
            'columnDefs': [{'visible': false, 'targets': [0]}, {
                'render': function (data, type, row) {
                    return '';
                }
            }]
        });

        new simpleDatatables.DataTable(tablee, {
            searchable: false,
            paging: false,
            "responsive": true
        });

    } else if (url == 'bulkearningsordeductions') {

        $(tablee).DataTable({
            "responsive": true,
            "bLengthChange": false,
            "bFilter": false,
            "bInfo": false,
            "autoWidth": false,
            "bPaginate": false,
            pageLength: 7,
            'columnDefs': [{'visible': false, 'targets': [0]}, {
                'render': function (data, type, row) {
                    return '';
                }
            }]
        });

        const dataTableSearch = new simpleDatatables.DataTable(tablee, {
            searchable: false,
            paging: false,
            "responsive": true
        });


        $(tablee).on('keyup', 'input', function (e) {

            if (e.keyCode == 13) {

                var table = $(tablee).DataTable();
                var cell = $(this).closest('td');

                //update the input value
                $(this).attr('value', $(this).val());

                //invalidate the DT cache
                table.cell($(cell)).invalidate().draw(false);


                var col = table.cell(cell).index().column;
                $(this).closest('tr').next('tr').find('td').eq(2).find('input').focus();
            }

        });
    } else {

        $(tablee).DataTable({
            "responsive": true,
            "lengthChange": false,
            "bDestroy": true,
            "aaSorting": [[2, 'desc']],
            "autoWidth": false,
            pageLength: 7,
            'columnDefs': [{'visible': false, 'targets': [1]}, {
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
        });

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


        id = data[0];

        if (url == 'individualemployeeallowances' || url == 'individualemployeedeductionss') {
            dynamicformsubmit(Form, "/api/hr/getemployeeallowanceordeduction/", id, 'Londonstudents', Modal);
            $(Modal).modal('show');
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


        id = data[0];
        $("#confirmText").html("Do you want to delete this record");
        $('#confirmModal').modal('show');

    });


    $(tablee + ' tbody').on('click', '#btnhorizonttalpayslip', function () {
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


        id = data[0];
        var indexed_array = {};
        indexed_array['id'] = payrollbeingprinted;
        indexed_array['payno'] = data[0];

        console.log(indexed_array)


        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: "/api/hr/horizontalpayslipindividual",
            data: JSON.stringify(indexed_array),
            success: function (result) {
                var sampleArr = base64ToArrayBuffer(result);
                var file = new Blob([sampleArr], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
                $("#modalprogress2").modal("hide");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#modalprogress2").modal("hide");
                $("#modalprogress2").modal("hide");
            }
        });


    });


    $(tablee + ' tbody').on('click', '#btnverticalpayslip', function () {
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


        id = data[0];
        var indexed_array = {};
        indexed_array['id'] = payrollbeingprinted;
        indexed_array['payno'] = data[0];

        console.log(indexed_array)


        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: "/api/hr/verticalpayslipindividual",
            data: JSON.stringify(indexed_array),
            success: function (result) {
                var sampleArr = base64ToArrayBuffer(result);
                var file = new Blob([sampleArr], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
                $("#modalprogress2").modal("hide");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#modalprogress2").modal("hide");
                $("#modalprogress2").modal("hide");
            }
        });


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


        id = data[0];
        if (url == 'individualemployeeallowances' || url == 'individualemployeedeductionss') {

            $.ajax({
                contentType: "application/json",
                url: "/api/hr/approverefundorrejectalowanceordeduction/" + id + "/Approved",
                dataType: 'json',
                success: function (result) {

                    $.ajax({
                        url: '/api/hr/getlistofemployees',
                        contentType: "application/json",
                        success: function (data) {
                            searchitems.length = 0;
                            listofstudents = JSON.parse(data);

                            $("#listemployeeseditpayslip").empty();

                            for (var i = 0; i < listofstudents.length; i++) {
                                $('<article id=' + listofstudents[i].id + ' onclick="getemployeepayslip(' + listofstudents[i].id + ')" class="leaderboard__profile "><img id="clickedemployeeimage" src=' + listofstudents[i].simage + ' class="leaderboard__picture"><span id="clickedemployeename" class="leaderboard__name">' + listofstudents[i].Name + '</span><span id="clickedemployeeamount" class="leaderboard__value">' + listofstudents[i].amount + '<span>kes</span></span></article>').appendTo('#listemployeeseditpayslip');
                            }

                            $('#chargepayee').prop('checked', false);
                            $('#exceptpayee').prop('checked', false);
                            $('#chargenhif').prop('checked', false);
                            $('#exceptnhif').prop('checked', false);
                            $('#chargenssf').prop('checked', false);
                            $('#exceptnssf').prop('checked', false);

                            $("img").on('error', function () {
                                $(this).attr("src", "https://www.shutterstock.com/image-vector/male-user-icon-vector-260nw-175066871.jpg");
                            });


                            getemployeepayslip(payno);
                        }
                    });


                },
                error: function (xhr, ajaxOptions, thrownError) {
                    $("#modalprogress2").modal("hide");
                }
            });


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


        id = data[0];
        if (url == 'individualemployeeallowances' || url == 'individualemployeedeductionss') {

            $.ajax({
                contentType: "application/json",
                url: "/api/hr/approverefundorrejectalowanceordeduction/" + id + "/Declined",
                success: function (result) {

                    $.ajax({
                        url: '/api/hr/getlistofemployees',
                        contentType: "application/json",
                        success: function (data) {
                            searchitems.length = 0;
                            listofstudents = JSON.parse(data);

                            $("#listemployeeseditpayslip").empty();

                            for (var i = 0; i < listofstudents.length; i++) {
                                $('<article id=' + listofstudents[i].id + ' onclick="getemployeepayslip(' + listofstudents[i].id + ')" class="leaderboard__profile "><img id="clickedemployeeimage" src=' + listofstudents[i].simage + ' class="leaderboard__picture"><span id="clickedemployeename" class="leaderboard__name">' + listofstudents[i].Name + '</span><span id="clickedemployeeamount" class="leaderboard__value">' + listofstudents[i].amount + '<span>kes</span></span></article>').appendTo('#listemployeeseditpayslip');
                            }

                            $('#chargepayee').prop('checked', false);
                            $('#exceptpayee').prop('checked', false);
                            $('#chargenhif').prop('checked', false);
                            $('#exceptnhif').prop('checked', false);
                            $('#chargenssf').prop('checked', false);
                            $('#exceptnssf').prop('checked', false);

                            $("img").on('error', function () {
                                $(this).attr("src", "https://www.shutterstock.com/image-vector/male-user-icon-vector-260nw-175066871.jpg");
                            });


                            getemployeepayslip(payno);
                        }
                    });


                },
                error: function (xhr, ajaxOptions, thrownError) {
                    $("#modalprogress2").modal("hide");
                }
            });


        }

    });


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


function getdeduction(id) {
    dynamicformsubmit("#deductionsform", "/api/hr/getDeduction/", id, 'Londonstudents', "#deductionmodal");
}

function getallowance(id) {
    dynamicformsubmit("#allowancesform", "/api/hr/getAllowance/", id, 'Londonstudents', "#allowancemodal");
}


function getemployee(id) {
    dynamicformsubmit("#employeesform", "/api/hr/getemployee/", id, 'Londonstudents', "#newemployeemodal");
}

function getkra(id) {
    dynamicformsubmit("#kraform", "/api/hr/getkraband/", id, 'Londonstudents', "#kramodal");
}

function getnhif(id) {
    dynamicformsubmit("#nhifform", "/api/hr/getnhifband/", id, 'Londonstudents', "#nhifmodal");
}

function getnssf(id) {
    dynamicformsubmit("#nssfform", "/api/hr/getnssfband/", id, 'Londonstudents', "#nssfmodal");
}

function gethousing(id) {
    dynamicformsubmit("#housingform", "/api/hr/gethousingband/", id, 'Londonstudents', "#housingmodal");
}

function getleave(id) {
    dynamicformsubmit("#leaveform", "/api/hr/getleave/", id, 'Londonstudents', "#leavemodal");
}


function getebulkreadingsrefresh() {

    var indexed_array = {};
    indexed_array['item'] = itemtoupdate;

    var date = new Date($('#bulkinsertdate').val());

    var month = date.getMonth() + 1;
    var year = date.getFullYear();

    indexed_array['month'] = month;
    indexed_array['year'] = year;

    monthname = monthNames[date.getMonth()];
    $("#modalbulkearningtitle").html(itemtoupdatename + ' Readings for the month of ' + monthname + '  ' + year);

    $.ajax({

        type: "POST",
        url: '/api/hr/getbulkinsertvalues',
        contentType: "application/json",
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (data) {
            dynamictable(data, '#bulkinsertearnings', 'bulkearningsordeductions', '#example1', '#example1', '#bulkearningmodal', '#bulkinsertearninform');

        }
    });

}

const monthNames = ["January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
];

var monthname;

var itemtoupdate;
var itemtoupdatename;

function getebulkreadings(id, name) {

    var indexed_array = {};
    indexed_array['item'] = id;
    itemtoupdate = id;
    itemtoupdatename = name;


    var date = new Date($('#bulkinsertdate').val());

    monthname = monthNames[date.getMonth()];

    var month = date.getMonth() + 1;
    var year = date.getFullYear();

    indexed_array['month'] = month;
    indexed_array['year'] = year;

    $("#modalbulkearningtitle").html(name + ' Readings for the month of ' + monthname + '  ' + year);


    $.ajax({

        type: "POST",
        url: '/api/hr/getbulkinsertvalues',
        contentType: "application/json",
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (data) {
            dynamictable(data, '#bulkinsertearnings', 'bulkearningsordeductions', '#example1', '#example1', '#bulkearningmodal', '#bulkinsertearninform');

        }
    });

}


function getebulkreadingsdeduct(id, name) {

    var indexed_array = {};
    indexed_array['item'] = id;
    itemtoupdate = id;
    itemtoupdatename = name;


    var date = new Date($('#bulkinsertdate').val());

    monthname = monthNames[date.getMonth()];

    var month = date.getMonth() + 1;
    var year = date.getFullYear();

    indexed_array['month'] = month;
    indexed_array['year'] = year;

    $("#modalbulkearningtitle").html(name + ' Readings for the month of ' + monthname + '  ' + year);


    $.ajax({

        type: "POST",
        url: '/api/hr/getbulkinsertvaluesdeductions',
        contentType: "application/json",
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (data) {
            dynamictable(data, '#bulkinsertearnings', 'bulkearningsordeductions', '#example1', '#example1', '#bulkearningmodal', '#bulkinsertearninform');

        }
    });

}


function getebulkreadingsrefreshdeductions() {

    var indexed_array = {};
    indexed_array['item'] = itemtoupdate;

    var date = new Date($('#bulkinsertdate').val());

    var month = date.getMonth() + 1;
    var year = date.getFullYear();

    indexed_array['month'] = month;
    indexed_array['year'] = year;

    monthname = monthNames[date.getMonth()];
    $("#modalbulkearningtitle").html(itemtoupdatename + ' Readings for the month of ' + monthname + '  ' + year);

    $.ajax({

        type: "POST",
        url: '/api/hr/getbulkinsertvaluesdeductions',
        contentType: "application/json",
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (data) {
            dynamictable(data, '#bulkinsertearnings', 'bulkearningsordeductions', '#example1', '#example1', '#bulkearningmodal', '#bulkinsertearninform');

        }
    });

}


function getemployeesdata() {

    $.ajax({
        url: '/api/hr/getlistofemployees',
        contentType: "application/json",
        success: function (data) {
            searchitems.length = 0;
            listofstudents = JSON.parse(data);

            $("#listemployeeseditpayslip").empty();

            for (var i = 0; i < listofstudents.length; i++) {
                $('<article id=' + listofstudents[i].id + ' onclick="getemployeepayslip(' + listofstudents[i].id + ')" class="leaderboard__profile "><img id="clickedemployeeimage" src=' + listofstudents[i].simage + ' class="leaderboard__picture"><span id="clickedemployeename" class="leaderboard__name">' + listofstudents[i].Name + '</span><span id="clickedemployeeamount" class="leaderboard__value">' + listofstudents[i].amount + '<span>kes</span></span></article>').appendTo('#listemployeeseditpayslip');
            }

            $('#chargepayee').prop('checked', false);
            $('#exceptpayee').prop('checked', false);
            $('#chargenhif').prop('checked', false);
            $('#exceptnhif').prop('checked', false);
            $('#chargenssf').prop('checked', false);
            $('#exceptnssf').prop('checked', false);

            $("img").on('error', function () {
                $(this).attr("src", "https://www.shutterstock.com/image-vector/male-user-icon-vector-260nw-175066871.jpg");
            });
        }
    });


}


function getemployeepayslip(idp) {


    $.ajax({
        contentType: "application/json",
        url: "/api/hr/getemployeegrosssalary/" + idp,
        dataType: 'json',
        success: function (result) {
            var object = result[0];


            $("#imageemployebeingedited").attr("src", object['simage']);
            $("#nameemployebeingedited").html(object['Name']);
            $("#valueemployebeingedited").html(object['amount']);

            if (object['paye'] == '1') {
                $('#chargepayee').prop('checked', true);
                $('#exceptpayee').prop('checked', false);
            } else {
                $('#chargepayee').prop('checked', false);
                $('#exceptpayee').prop('checked', true);
            }
            if (object['nhif'] == '1') {
                $('#chargenhif').prop('checked', true);
                $('#exceptnhif').prop('checked', false);
            } else {
                $('#chargenhif').prop('checked', false);
                $('#exceptnhif').prop('checked', true);
            }
            if (object['nssf'] == '1') {
                $('#chargenssf').prop('checked', true);
                $('#exceptnssf').prop('checked', false);
            } else {
                $('#chargenssf').prop('checked', false);
                $('#exceptnssf').prop('checked', true);
            }


            var indexed_array = {};
            indexed_array['id'] = idp;
            id = idp
            payno = idp;

            $.ajax({

                type: "POST",
                contentType: "application/json",
                url: '/api/hr/getemployeeallowances',
                data: JSON.stringify(indexed_array),
                dataType: 'json',
                success: function (result) {


                    dynamictable(result, '#divemployeeallowances', 'individualemployeeallowances', '#example1', '#example1', '#modalemployeeearning', '#employeeallowanceform');


                    $.ajax({

                        type: "POST",
                        contentType: "application/json",
                        url: '/api/hr/getemployeedeductions',
                        data: JSON.stringify(indexed_array),
                        dataType: 'json',
                        success: function (result) {

                            dynamictable(result, '#divemployeedeductions', 'individualemployeedeductionss', '#example2', '#example2', '#modalemployeedeductions', '#employeedeductionsform');

                        },
                        error: function (e) {
                            //console.log("ERROR: ", e);
                        }
                    });


                },
                error: function (e) {
                    //console.log("ERROR: ", e);
                }
            });


        },
        error: function (xhr, ajaxOptions, thrownError) {
            $("#modalprogress2").modal("hide");
        }
    });


}


function getPayroll(id) {
    dynamicformsubmit("#payrollform", "/api/hr/getPayroll/", id, 'Londonstudents', "#payrollmodal");
}


function makepayrollpayment(id) {
    $("#payroll").val(id);
    payrollbeingprinted = id;
}


$('#dark-version').click(function () {

    var indexed_array = {};

    if ($('#dark-version').is(":checked")) {
        indexed_array['value'] = 1;
    } else {
        indexed_array['value'] = 0;
    }


    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: '/api/hr/updateusertheme',
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (result) {
            location.reload();

        }
    });


});


var theme;

$(document).ready(function () {

    $("#payrolltable").DataTable({
        "responsive": true,
        "bLengthChange": false,
        "bFilter": false,
        "aaSorting": [[2, 'desc']],
        "bInfo": false,
        "bPaginate": false,
        pageLength: 7,
        'columnDefs': [{'visible': false, 'targets': [0]}, {
            'render': function (data, type, row) {
                return '';
            }
        }]
    });

    $.ajax({
        contentType: "application/json",
        url: "/api/hr/gettheme",
        dataType: 'json',
        success: function (result) {
            var object = result[0];
            if (object['theme'] == '1') {
                $('#dark-version').prop('checked', true);
                darkMode(object['theme']);
                theme = 'dark-version';
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            $("#modalprogress2").modal("hide");
        }

    });


    $('#yesBtn').click(function (e) {


        link = $(this);

        dynamicdeleteitem(id, tablebeingedited, link.attr("href"));

        e.preventDefault();
        $('#confirmModal').modal('hide');
    });

});


function printsalariestotebankcsv() {
    printoption = 'salariestothebankscv';
    openTestModal();
}


function printsalariestotebank() {
    printoption = 'salariestothebank';
    openTestModal();
}


function Deductions_Report() {
    printoption = 'Deductions_Report';
    openTestModal();
}


function Earnings_Report() {
    printoption = 'Earnings_Report';
    openTestModal();
}


function openTestModal() {
    $("#modalprogress2").modal("show");
}


var printoption;

function printpayregfrombasictogross() {
    printoption = 'payregisterfrombasictogross';
    openTestModal();
}


function printpayregfromgrosstonet() {
    printoption = 'printpayregfromgrosstonet';
    openTestModal();
}

function printpayregcombined() {
    printoption = 'printpayregcombined';
    openTestModal();
}

function printhorizontalpayslips() {
    printoption = 'printhorizontalpayslips';
    openTestModal();
}

function printverticalpayslips() {
    printoption = 'printverticalpayslips';
    openTestModal();
}

function printpayeereturns() {
    printoption = 'printpayeereturns';
    openTestModal();
}

function p9a() {
    printoption = 'p9a';
    openTestModal();
}

function p10a() {
    printoption = 'p10a';
    openTestModal();
}

function p9() {
    printoption = 'p9';
    openTestModal();
}


function nhif() {
    printoption = 'nhif';
    openTestModal();
}

function nssf() {
    printoption = 'nssf';
    openTestModal();
}

function housing() {
    printoption = 'housing';
    openTestModal();
}

function nita() {
    printoption = 'nita';
    openTestModal();
}

function banktotals() {
    printoption = 'banktotals';
    openTestModal();
}

function payrolltotals() {
    printoption = 'payrolltotals';
    openTestModal();
}

function netpay() {
    printoption = 'netpay';
    openTestModal();
}


function netgativepay() {
    printoption = 'netgativepay';
    openTestModal();
}


$('#itembank').on('change', function () {

    if ($(this).val()) {
        fetchbankamount($(this).val())
    }

});

function fetchbankamount(bankid) {

    var indexed_array = {};
    indexed_array['bank'] = bankid;
    indexed_array['id'] = payrollbeingprinted;

    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: "/api/hr/getbankamount",
        data: JSON.stringify(indexed_array),
        success: function (result) {
            var object = JSON.parse(result)[0];
            $("#amount").val(object['amount']);
        }
    });
}


function formatNumberWithSeparator(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}


$('#modalprogress2').on('shown.bs.modal', function (e) {

    if (printoption == 'payregisterfrombasictogross') {

        var indexed_array = {};
        indexed_array['id'] = payrollbeingprinted;


        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: "/api/hr/printregisterfrombasictogross",
            data: JSON.stringify(indexed_array),
            success: function (result) {
                var sampleArr = base64ToArrayBuffer(result);
                var file = new Blob([sampleArr], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
                $("#modalprogress2").modal("hide");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#modalprogress2").modal("hide");
                $("#modalprogress2").modal("hide");
            }
        });


    } else if (printoption == 'printpayregfromgrosstonet') {

        var indexed_array = {};
        indexed_array['id'] = payrollbeingprinted;


        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: "/api/hr/printpayregfromgrosstonet",
            data: JSON.stringify(indexed_array),
            success: function (result) {
                var sampleArr = base64ToArrayBuffer(result);
                var file = new Blob([sampleArr], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
                $("#modalprogress2").modal("hide");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#modalprogress2").modal("hide");
                $("#modalprogress2").modal("hide");
            }
        });


    } else if (printoption == 'printpayregcombined') {

        var indexed_array = {};
        indexed_array['id'] = payrollbeingprinted;


        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: "/api/hr/printpayregcombined",
            data: JSON.stringify(indexed_array),
            success: function (result) {
                var sampleArr = base64ToArrayBuffer(result);
                var file = new Blob([sampleArr], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
                $("#modalprogress2").modal("hide");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#modalprogress2").modal("hide");
                $("#modalprogress2").modal("hide");
            }
        });


    }





    // Payslips

    else if (printoption == 'printhorizontalpayslips') {

        var indexed_array = {};
        indexed_array['id'] = payrollbeingprinted;


        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: "/api/hr/horizontalpayslips",
            data: JSON.stringify(indexed_array),
            success: function (result) {
                var sampleArr = base64ToArrayBuffer(result);
                var file = new Blob([sampleArr], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
                $("#modalprogress2").modal("hide");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#modalprogress2").modal("hide");
                $("#modalprogress2").modal("hide");
            }
        });


    } else if (printoption == 'printverticalpayslips') {

        var indexed_array = {};
        indexed_array['id'] = payrollbeingprinted;


        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: "/api/hr/verticalpayslips",
            data: JSON.stringify(indexed_array),
            success: function (result) {
                var sampleArr = base64ToArrayBuffer(result);
                var file = new Blob([sampleArr], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
                $("#modalprogress2").modal("hide");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#modalprogress2").modal("hide");
                $("#modalprogress2").modal("hide");
            }
        });


    }


    // Tax reports


    else if (printoption == 'printpayeereturns') {

        var indexed_array = {};
        indexed_array['id'] = payrollbeingprinted;


        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: "/api/hr/payeereturns",
            data: JSON.stringify(indexed_array),
            success: function (result) {
                var sampleArr = base64ToArrayBuffer(result);
                var file = new Blob([sampleArr], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
                $("#modalprogress2").modal("hide");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#modalprogress2").modal("hide");
                $("#modalprogress2").modal("hide");
            }
        });


    } else if (printoption == 'p9a') {

        var indexed_array = {};
        indexed_array['id'] = payrollbeingprinted;


        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: "/api/hr/p9a",
            data: JSON.stringify(indexed_array),
            success: function (result) {
                var sampleArr = base64ToArrayBuffer(result);
                var file = new Blob([sampleArr], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
                $("#modalprogress2").modal("hide");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#modalprogress2").modal("hide");
                $("#modalprogress2").modal("hide");
            }
        });


    } else if (printoption == 'p10a') {

        var indexed_array = {};
        indexed_array['id'] = payrollbeingprinted;


        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: "/api/hr/p10a",
            data: JSON.stringify(indexed_array),
            success: function (result) {
                var sampleArr = base64ToArrayBuffer(result);
                var file = new Blob([sampleArr], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
                $("#modalprogress2").modal("hide");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#modalprogress2").modal("hide");
                $("#modalprogress2").modal("hide");
            }
        });


    } else if (printoption == 'p9') {

        var indexed_array = {};
        indexed_array['id'] = payrollbeingprinted;


        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: "/api/hr/p9",
            data: JSON.stringify(indexed_array),
            success: function (result) {
                var sampleArr = base64ToArrayBuffer(result);
                var file = new Blob([sampleArr], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
                $("#modalprogress2").modal("hide");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#modalprogress2").modal("hide");
                $("#modalprogress2").modal("hide");
            }
        });


    } else if (printoption == 'nhif') {

        var indexed_array = {};
        indexed_array['id'] = payrollbeingprinted;


        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: "/api/hr/nhif",
            data: JSON.stringify(indexed_array),
            success: function (result) {
                var sampleArr = base64ToArrayBuffer(result);
                var file = new Blob([sampleArr], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
                $("#modalprogress2").modal("hide");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#modalprogress2").modal("hide");
                $("#modalprogress2").modal("hide");
            }
        });


    } else if (printoption == 'nssf') {

        var indexed_array = {};
        indexed_array['id'] = payrollbeingprinted;


        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: "/api/hr/nssf",
            data: JSON.stringify(indexed_array),
            success: function (result) {
                var sampleArr = base64ToArrayBuffer(result);
                var file = new Blob([sampleArr], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
                $("#modalprogress2").modal("hide");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#modalprogress2").modal("hide");
                $("#modalprogress2").modal("hide");
            }
        });


    } else if (printoption == 'housing') {

        var indexed_array = {};
        indexed_array['id'] = payrollbeingprinted;


        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: "/api/hr/housing",
            data: JSON.stringify(indexed_array),
            success: function (result) {
                var sampleArr = base64ToArrayBuffer(result);
                var file = new Blob([sampleArr], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
                $("#modalprogress2").modal("hide");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#modalprogress2").modal("hide");
                $("#modalprogress2").modal("hide");
            }
        });


    } else if (printoption == 'nita') {

        var indexed_array = {};
        indexed_array['id'] = payrollbeingprinted;


        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: "/api/hr/nita",
            data: JSON.stringify(indexed_array),
            success: function (result) {
                var sampleArr = base64ToArrayBuffer(result);
                var file = new Blob([sampleArr], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
                $("#modalprogress2").modal("hide");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#modalprogress2").modal("hide");
                $("#modalprogress2").modal("hide");
            }
        });


    } else if (printoption == 'banktotals') {

        var indexed_array = {};
        indexed_array['id'] = payrollbeingprinted;


        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: "/api/hr/banktotals",
            data: JSON.stringify(indexed_array),
            success: function (result) {
                var sampleArr = base64ToArrayBuffer(result);
                var file = new Blob([sampleArr], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
                $("#modalprogress2").modal("hide");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#modalprogress2").modal("hide");
                $("#modalprogress2").modal("hide");
            }
        });


    } else if (printoption == 'payrolltotals') {

        var indexed_array = {};
        indexed_array['id'] = payrollbeingprinted;


        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: "/api/hr/payrolltotals",
            data: JSON.stringify(indexed_array),
            success: function (result) {
                var sampleArr = base64ToArrayBuffer(result);
                var file = new Blob([sampleArr], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
                $("#modalprogress2").modal("hide");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#modalprogress2").modal("hide");
                $("#modalprogress2").modal("hide");
            }
        });


    } else if (printoption == 'salariestothebank') {

        var indexed_array = {};
        indexed_array['id'] = payrollbeingprinted;
        indexed_array['itembank'] = $('#itembanknotes').val();


        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: "/api/hr/notestothebank",
            data: JSON.stringify(indexed_array),
            success: function (result) {
                var sampleArr = base64ToArrayBuffer(result);
                var file = new Blob([sampleArr], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
                $("#modalprogress2").modal("hide");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#modalprogress2").modal("hide");
                $("#modalprogress2").modal("hide");
            }
        });


    } else if (printoption == 'salariestothebankscv') {

        var indexed_array = {};
        indexed_array['id'] = payrollbeingprinted;
        indexed_array['itembank'] = $('#itembankcsv').val();


        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: "/api/hr/notestothebankexcel",
            data: JSON.stringify(indexed_array),
            success: function (result) {
                var sampleArr = base64ToArrayBuffer(result);
                var file = new Blob([sampleArr], {type: 'text/csv'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
                $("#modalprogress2").modal("hide");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#modalprogress2").modal("hide");
                $("#modalprogress2").modal("hide");
            }
        });


    } else if (printoption == 'Earnings_Report') {

        var indexed_array = {};
        indexed_array['id'] = payrollbeingprinted;
        indexed_array['item'] = $('#itemearning').val();


        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: "/api/hr/Earnings_Report",
            data: JSON.stringify(indexed_array),
            success: function (result) {
                var sampleArr = base64ToArrayBuffer(result);
                var file = new Blob([sampleArr], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
                $("#modalprogress2").modal("hide");
                $("#earningssreportmodal").modal("hide");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#modalprogress2").modal("hide");
            }
        });


    } else if (printoption == 'Deductions_Report') {

        var indexed_array = {};
        indexed_array['id'] = payrollbeingprinted;
        indexed_array['item'] = $('#itemdeduction').val();


        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: "/api/hr/Deductions_Report",
            data: JSON.stringify(indexed_array),
            success: function (result) {
                var sampleArr = base64ToArrayBuffer(result);
                var file = new Blob([sampleArr], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
                $("#modalprogress2").modal("hide");
                $("#deductionsreportmodal").modal("hide");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#modalprogress2").modal("hide");
            }
        });


    } else if (printoption == 'netpay') {

        var indexed_array = {};
        indexed_array['id'] = payrollbeingprinted;


        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: "/api/hr/netpay",
            data: JSON.stringify(indexed_array),
            success: function (result) {
                var sampleArr = base64ToArrayBuffer(result);
                var file = new Blob([sampleArr], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
                $("#modalprogress2").modal("hide");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#modalprogress2").modal("hide");
                $("#modalprogress2").modal("hide");
            }
        });


    } else if (printoption == 'netgativepay') {

        var indexed_array = {};
        indexed_array['id'] = payrollbeingprinted;


        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: "/api/hr/netgativepay",
            data: JSON.stringify(indexed_array),
            success: function (result) {
                var sampleArr = base64ToArrayBuffer(result);
                var file = new Blob([sampleArr], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
                $("#modalprogress2").modal("hide");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#modalprogress2").modal("hide");
                $("#modalprogress2").modal("hide");
            }
        });


    }


});


function openmodal(modal, mform) {
    $(mform)[0].reset();
    id = '';
    $(mform).find("#id").val("");
    $(modal).modal('show');
}


function opendefaultmodal() {

    actionsaveorupdate = 'Save';

    console.log(modaltoopen);

    //$(myform)[0].reset();


    if (modaltoopen == "#newemployeemodal") {


        $(modaltoopen).modal('show');


    }

    $("#formsubmitbuttemployees").html("Save");

}


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
                if (id == 'itemallowance') {
                    itemallowance.setChoiceByValue(value);
                } else if (id == 'itemdeduction') {
                    itemdeduction.setChoiceByValue(value);
                } else if (id == 'employeetype') {
                    employeetypeselect.setChoiceByValue(value);
                    if (value == 'Teacher') {

                        $("#listofteachersemployeediv").show();
                        $("#listofstaffemployeediv").hide();

                        $("#linktostaff").val('');
                    } else if (value == 'Support staff') {
                        $("#listofteachersemployeediv").hide();
                        $("#listofstaffemployeediv").show();

                        $("#linktoteacher").val('');
                    } else if (value == 'Other') {

                        $("#listofteachersemployeediv").hide();
                        $("#listofstaffemployeediv").hide();

                        $("#listofstaffemployeediv").val('');
                        $("#listofstaffemployeediv").val('');
                    }
                } else if (id == 'gender') {
                    employeegenderselect.setChoiceByValue(value);
                } else if (id == 'paymeth') {
                    employeepaymethselect.setChoiceByValue(value);
                } else if (id == 'housinglev') {
                    employeepaymethselect.setChoiceByValue(value);
                } else if (id == 'nssfoptionperband') {
                    nssfoptionperband.setChoiceByValue(value);
                } else if (id == 'linktoteacher') {
                    employeelinktoteacherselect.setChoiceByValue(value);
                } else if (id == 'linktostaff') {
                    employeelinktostaffselect.setChoiceByValue(value);
                } else if (id == 'visiblealowance') {
                    visiblealowance.setChoiceByValue(value);
                } else if (id == 'allowancetype') {
                    allowancetype.setChoiceByValue(value);

                    if (value == '1') {
                        $("#divcostperunit").show();
                    } else if (value == '0') {
                        $("#divcostperunit").hide();
                    }
                } else if (id == 'visiblededuction') {
                    visiblededuction.setChoiceByValue(value);
                } else if (id == 'calculationtype') {
                    calculationtype.setChoiceByValue(value);
                } else if (id == 'deductiontype') {
                    deductiontype.setChoiceByValue(value);
                    console.log(value);
                    if (value == '1') {
                        $("#divcostperunit").show();
                    } else if (value == '0') {
                        $("#divcostperunit").hide();
                    }
                }

                break;
            default:
                ctrl.val(value);

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


function getlistofemployees() {

    $.ajax({
        url: '/api/hr/getlistofemployees',
        contentType: "application/json",
        success: function (data) {
            searchitems.length = 0;
            listofstudents = JSON.parse(data);
            for (var i = 0; i < listofstudents.length; i++) {
                searchitems.push(listofstudents[i].Name);
            }

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


$("#searchemployee").autocomplete({
    source: function (request, response) {
        console.log('mce ' + searchitems.length);

        var results = $.ui.autocomplete.filter(searchitems, request.term);
        response(results.slice(0, 10));
    },
    minLength: 1,
    appendTo: "#leavemodal",
    select: function (event, ui) {

        $("#searchemployee").val((ui.item.value))

    }

});
$("#searchemployee").autocomplete("widget").attr('style', 'max-height: 400px;max-width: 500px;font-size:12px;overflow-y: auto; overflow-x: hidden;')


$("#formlistofemployeeupdatepayroll").autocomplete({
    source: function (request, response) {

        var input, filter, a, i, txtValue;
        input = document.getElementById("formlistofemployeeupdatepayroll");
        filter = input.value.toUpperCase();
        box1 = document.getElementById("listemployeeseditpayslip");
        a = box1.getElementsByTagName("article");
        for (i = 0; i < a.length; i++) {
            p = a[i].getElementsByTagName("span")[0];
            txtValue = p.textContent || p.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                a[i].style.display = "";
            } else {
                a[i].style.display = "none";
            }
        }

    },
    minLength: 1,
    appendTo: "#modalemployeepayslip",
    select: function (event, ui) {
        $("#formlistofemployeeupdatepayroll").val((ui.item.value))
    }

});


function reloadcurrentpage() {
    location.reload();
}


$('#setsystemtouseratioforpaye').click(function () {


    var indexed_array = {};

    indexed_array['value'] = 0;

    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: '/api/hr/updatepayeeoption',
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (result) {


        }
    });
});


$('#setsystemtouseamountforpaye').click(function () {

    var indexed_array = {};

    indexed_array['value'] = 1;

    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: '/api/hr/updatepayeeoption',
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (result) {


        }
    });
});


$('#usebiometricsforpayroll').click(function () {

    var indexed_array = {};

    if ($('#usebiometricsforpayroll').is(":checked")) {
        indexed_array['value'] = 1;
    } else {
        indexed_array['value'] = 0;
    }

    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: '/api/hr/updatebiometricsoption',
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (result) {


        }
    });


});


$('#useratiofornhif').click(function () {

    var indexed_array = {};

    if ($('#useratiofornhif').is(":checked")) {
        $('#useamountfornhif').prop('checked', false);
        indexed_array['value'] = 0;
    } else {
        $('#useamountfornhif').prop('checked', true);
        indexed_array['value'] = 1;
    }

    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: '/api/hr/updatenhifoption',
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (result) {


        }
    });


});


$('#useamountfornhif').click(function () {

    var indexed_array = {};

    if ($('#useamountfornhif').is(":checked")) {
        $('#useratiofornhif').prop('checked', false);
        indexed_array['value'] = 1;
    } else {
        $('#useratiofornhif').prop('checked', true);
        indexed_array['value'] = 0;
    }

    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: '/api/hr/updatenhifoption',
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (result) {


        }
    });
});


$('#useratiofornssf').click(function () {


    var indexed_array = {};

    indexed_array['value'] = 0;

    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: '/api/hr/updatenssfoption',
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (result) {


        }
    });
});


$('#useamountfornssf').click(function () {

    var indexed_array = {};

    indexed_array['value'] = 1;

    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: '/api/hr/updatenssfoption',
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (result) {


        }
    });
});


$("#employeetaxnumber").keypress(function (e) {
    if (e.which == 13) {
        var indexed_array = {};

        indexed_array['value'] = $("#employeetaxnumber").val();
        indexed_array['type'] = 'taxnu';

        $('#buttonnotifications').click();

        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: '/api/hr/updateotherpayrosettings',
            data: JSON.stringify(indexed_array),
            dataType: 'json',
            success: function (result) {

            }
        });

    }
});


$("#taxrelief").keypress(function (e) {
    if (e.which == 13) {
        var indexed_array = {};

        indexed_array['value'] = $("#taxrelief").val();
        indexed_array['type'] = 'taxrelief';

        $('#buttonnotifications').click();

        $.ajax({

            type: "POST",
            contentType: "application/json",
            url: '/api/hr/updateotherpayrosettings',
            data: JSON.stringify(indexed_array),
            dataType: 'json',
            success: function (result) {

            }
        });

    }
});


$('#chargepayee').click(function () {

    var indexed_array = {};

    indexed_array['value'] = 1;
    indexed_array['id'] = payno;

    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: '/api/hr/chargeorexcemptemployeepayee',
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (result) {


        }
    });
});


$('#exceptpayee').click(function () {

    var indexed_array = {};

    indexed_array['value'] = 0;
    indexed_array['id'] = payno;

    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: '/api/hr/chargeorexcemptemployeepayee',
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (result) {


        }
    });
});


$('#chargenhif').click(function () {

    var indexed_array = {};

    indexed_array['value'] = 1;
    indexed_array['id'] = payno;

    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: '/api/hr/chargeorexcemptemployeenhif',
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (result) {


        }
    });
});


$('#exceptnhif').click(function () {

    var indexed_array = {};

    indexed_array['value'] = 0;
    indexed_array['id'] = payno;

    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: '/api/hr/chargeorexcemptemployeenhif',
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (result) {


        }
    });
});


$('#chargenssf').click(function () {

    var indexed_array = {};

    indexed_array['value'] = 1;
    indexed_array['id'] = payno;

    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: '/api/hr/chargeorexcemptemployeenssf',
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (result) {


        }
    });
});


$('#exceptnssf').click(function () {

    var indexed_array = {};

    indexed_array['value'] = 0;
    indexed_array['id'] = payno;

    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: '/api/hr/chargeorexcemptemployeenssf',
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (result) {


        }
    });
});


function submitForm(event, myform) {

    event.preventDefault();


    var unindexed_array = $('#' + myform).serializeArray();
    var indexed_array = {};


    if (myform == 'employeesform') {
        actionurl = "/api/hr/addorupdateemployee";
    } else if (myform == 'kraform') {
        actionurl = "/api/hr/addorupdatekra";
    } else if (myform == 'nssfform') {
        actionurl = "/api/hr/addorupdatenssf";
    } else if (myform == 'housingform') {
        actionurl = "/api/hr/addorupdatehousing";
    } else if (myform == 'nhifform') {
        actionurl = "/api/hr/addorupdatenhif";
    } else if (myform == 'leaveform') {
        actionurl = "/api/hr/addorupdateleave";
    } else if (myform == 'employeeallowanceform') {
        actionurl = "/api/hr/addorupdatemployeeallowanceordeduction";
        indexed_array['payno'] = payno;
        indexed_array['type'] = 'EARNING';
        if ($('#reccurrentalowancecheck').is(":checked")) {
            indexed_array['reccurrent'] = '1';
        } else {
            indexed_array['reccurrent'] = '0';
        }
    } else if (myform == 'employeedeductionsform') {
        actionurl = "/api/hr/addorupdatemployeeallowanceordeduction";
        indexed_array['payno'] = payno;
        indexed_array['type'] = 'DEDUCTION';
        if ($('#reccurrentalowancecheck').is(":checked")) {
            indexed_array['reccurrent'] = '1';
        } else {
            indexed_array['reccurrent'] = '0';
        }


    } else if (myform == 'payrollpaymentform') {
        actionurl = "/api/hr/makepayrollpayment";
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


            } else if (myform == 'employeedeductionsform') {

                getemployeepayslip(payno)
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


function loadnewemployeepage() {

    $.post("addnewemployee").done(function (fragment) {

        $.ajax({

            url: '/api/hr/loademployees',
            contentType: "application/json",
            success: function (data) {
                listofstudents = JSON.parse(data);
                dynamictable(listofstudents, '#div1', 'listofemployees', '#example1', '#example1', '#newemployeemodal', '#form');
                $("#modalprogress2").modal("hide");
            }
        });

        $("#output").replaceWith(fragment);

    });

}


function loademployees() {

    Myurl = "listofemployees";

    $.post("employees").done(function (fragment) {

        $.ajax({

            url: '/api/hr/loademployees',
            contentType: "application/json",
            success: function (data) {
                listofstudents = JSON.parse(data);
                dynamictable(listofstudents, '#div1', 'listofemployees', '#example1', '#example1', '#newemployeemodal', '#form');
                $("#modalprogress2").modal("hide");

            }
        });

        $("#output").replaceWith(fragment);

    });

}

function loadaddneweployeepage() {

    $.post("loadaddnewemployeepage").done(function (fragment) {

        $("#output").replaceWith(fragment);
        $('#output').show();


    });


}


$('#employeetype').on('change', function () {

    if ($(this).val() == 'Teacher') {
        $("#listofteachersemployeediv").show();
        $("#listofstaffemployeediv").hide();

        $("#linktostaff").val('');
    } else if ($(this).val() == 'Support staff') {
        $("#listofteachersemployeediv").hide();
        $("#listofstaffemployeediv").show();

        $("#linktoteacher").val('');
    } else {

        $("#listofteachersemployeediv").hide();
        $("#listofstaffemployeediv").hide();

        $("#listofstaffemployeediv").val('');
        $("#listofstaffemployeediv").val('');
    }

});


$('#allowancetype').on('change', function () {
    if ($(this).val() == '0') {
        $("#divcostperunit").hide();
    } else {
        console.log();
        $("#divcostperunit").show();
    }
});


$('#deductiontype').on('change', function () {
    if ($(this).val() == '0') {
        $("#divcostperunit").hide();
    } else {
        $("#divcostperunit").show();
    }
});


function postbulkearnings() {

    var tablemain = "#example1";


    var date = new Date($('#bulkinsertdate').val());
    var month = date.getMonth() + 1;
    var year = date.getFullYear();

    var table = $(maintable).DataTable();
    var employees = {values: [], item: [], month: [], year: []};
    employees.item.push(itemtoupdate);
    employees.month.push(month);
    employees.year.push(year);

    table.rows().every(function (rowIdx, tableLoop, rowLoop) {
        var Row = this.data();
        employees.values.push({
            "employeeid": Row[0],
            "item": itemtoupdate,
            "month": month,
            "year": year,
            "value": this.cell(rowIdx, 3).nodes().to$().find('input').val()
        });
    });


    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/api/hr/postbulkearnings",
        data: JSON.stringify(employees),
        success: function (data) {

            $("#bulkearningmodal").modal("hide");

            var object = JSON.parse(data)[0];
            $(function () {
                $("#messageid").html(object['querystatus']);
                $('#myModal').modal('show');
                setTimeout(function () {
                    $('#myModal').modal('hide');
                }, 1500);
            });


        },
        error: function (xhr, ajaxOptions, thrownError) {
            //console.log(thrownError+"  mceee  "+xhr);
        }
    });


}


function postbulkdeductions() {

    var tablemain = "#example1";


    var date = new Date($('#bulkinsertdate').val());
    var month = date.getMonth() + 1;
    var year = date.getFullYear();

    var table = $(maintable).DataTable();
    var employees = {values: [], item: [], month: [], year: []};
    employees.item.push(itemtoupdate);
    employees.month.push(month);
    employees.year.push(year);

    table.rows().every(function (rowIdx, tableLoop, rowLoop) {
        var Row = this.data();
        employees.values.push({
            "employeeid": Row[0],
            "item": itemtoupdate,
            "month": month,
            "year": year,
            "value": this.cell(rowIdx, 3).nodes().to$().find('input').val()
        });
    });


    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/api/hr/postbulkdeductions",
        data: JSON.stringify(employees),
        success: function (data) {

            $("#bulkearningmodal").modal("hide");

            var object = JSON.parse(data)[0];
            $(function () {
                $("#messageid").html(object['querystatus']);
                $('#myModal').modal('show');
                setTimeout(function () {
                    $('#myModal').modal('hide');
                }, 1500);
            });


        },
        error: function (xhr, ajaxOptions, thrownError) {
            //console.log(thrownError+"  mceee  "+xhr);
        }
    });


}


var payrollbeingprinted;


function getpayrolldata(id, payroll) {

    var indexed_array = {};
    indexed_array['id'] = id;

    payrollbeingprinted = id;

    $("#payrollmodaltitle").html(payroll + "  Payroll");


    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: '/api/hr/getpayrolldata',
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (result) {
            dynamictable(result, '#divpayrolldata', 'payrolldata', '#example4', '#example4', '#modalpayrolldataandreports', '');
        }
    });

}


function viewpayrollpayments(id, payroll) {

    var indexed_array = {};
    indexed_array['id'] = id;

    payrollbeingprinted = id;

    $("#payrollpaymentstitle").html(payroll + "  payments");


    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: '/api/hr/viewpayrollpayments',
        data: JSON.stringify(indexed_array),
        dataType: 'json',
        success: function (result) {
            dynamictable(result, '#payrollpaymentsdiv', 'payrollpayments', '#example4', '#example4', '#Viewpayrollpayments', '');
        }
    });

}


function viewempoyeeattendance() {


}