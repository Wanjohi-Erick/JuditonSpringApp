let selectedIssuedItem = null;
let monthlyCHART = null;
let theme;
var searchitems  =[];
function setActiveNav(clickedElement) {
    console.log(clickedElement);
    // remove active class from all nav items
    const navItems = document.querySelectorAll('.nav-link');
    navItems.forEach(navItem => {
        navItem.classList.remove('active');
        navItem.classList.remove('bg-gradient-primary');
    });

    // add active class to clicked nav item
    clickedElement.classList.add('active');
    clickedElement.classList.add('bg-gradient-primary');
}


$(document).ready(function () {
    $.ajax({
        contentType: "application/json",
        url: "/getTheme",
        dataType: 'json',
        success: function (data) {
            if (data.theme === 1) {
                $('#dark-version').prop('checked', true);
                let check = document.getElementById("dark-version");
                $('.form-select').addClass('text-white');
                darkMode(check);
                theme = 'dark-version';
            } else {
                $('.form-select').removeClass('text-white');
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            $("#modalprogress2").modal("hide");
        }

    });

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
            url: '/updateTheme',
            data: JSON.stringify(indexed_array),
            dataType: 'json',
            success: function (result) {
                console.log(result);
                //location.reload();
            }
        });
    });

    $("#searchTitle").on('input', function() {
        searchItem();
    });

    $("#searchInput").autocomplete({
        source: function (request, response) {
            var results = $.ui.autocomplete.filter(searchitems, request.term);
            response(results.slice(0, 10));
        },
        minLength: 1,
        appendTo: "#searchMemberDiv",
        select: function (event, ui) {

            var indexed_array = {};
            indexed_array['id'] = (ui.item.value).split(' ')[0];


            $.ajax({
                method: 'POST',
                url: '/api/member/api/reg/getstudent',
                data: JSON.stringify(indexed_array),
                contentType: 'application/json'
            }).done(function(data) {
                let object = JSON.parse(data);
                console.log(object);

                $("#recipientId").val(object[0].id);
                $("#searchInput").val(object[0].name);
                $("#searchInput").blur();
            }).fail(function (data) {
                console.log(data);
            })

            $(this).val('');
            return false;

        }
    });
    $("#searchInput").autocomplete("widget").attr('style', 'max-height: 400px;max-width: 265px;font-size:12px;overflow-y: auto; overflow-x: hidden;')
    $("#searchInput").click(function () {
        fetchAutoCompleteData();
    });
})

function fetchAutoCompleteData() {
    try {

        searchitems.length = 0;

        let indexed_array = {};
        $.ajax({
            method: 'POST',
            url: '/api/member/api/reg/getstudentautocompletedata',
            data: JSON.stringify(indexed_array),
            contentType: 'application/json',
            success: function (data) {
                console.log(data)
                var member = JSON.parse(data);
                for (var i = 0; i < member.length; i++) {
                    searchitems.push(member[i].member_number + " " + member[i].Name);
                }

            }
        });


    } catch (error) {
        console.error("Failed to fetch data", error);
    }
}

function getGroups() {
    return new Promise(function (resolve, reject) {
        $.ajax({
            url: '/api/inv/getAllItemGroups',
            success: function (data) {
                resolve(data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                reject(errorThrown);
            }
        });
    });
}

function getItems() {
    return new Promise(function (resolve, reject) {
        $.ajax({
            url: '/api/inv/getAllItems',
            success: function (data) {
                resolve(data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                reject(errorThrown);
            }
        });
    });
}

function getVendors() {
    return new Promise(function (resolve, reject) {
        $.ajax({
            url: '/api/inv/getAllVendors',
            success: function (data) {
                resolve(data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                reject(errorThrown);
            }
        });
    });
}


function initMonthInventoryChart(jsonData) {
    $("#inventoryActivityTitle").text("Inventory Activity Per Day");

    const data = jsonData;

    const dailyTotals = {};

    data.forEach((item) => {
        const date = item.day.substring(0, 7); // Change "day" to "date"
        if (dailyTotals.hasOwnProperty(date)) {
            dailyTotals[date].total += item.total; // Use "total" instead of "item_quantity"
            dailyTotals[date].quantity += item.item_quantity; // Add a new property "quantity"
        } else {
            dailyTotals[date] = {
                total: item.total,
                quantity: item.item_quantity
            };
        }
    });

    const days = Object.keys(dailyTotals);
    const totalQuantities = Object.values(dailyTotals).map((data) => data.quantity); // Use "quantity" as the data

    const ctx = document.getElementById('myChart').getContext('2d');

    if (monthlyCHART) {
        monthlyCHART.destroy();
    }

    monthlyCHART = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: days,
            datasets: [{
                label: 'Quantity', // Change the label for the vertical axis
                data: totalQuantities,
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Quantity' // Change the label for the vertical axis
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Date' // Add a label for the horizontal axis
                    }
                }
            }
        }
    });
}

function getMonthName(monthNumber) {
    const date = new Date(Date.UTC(2000, monthNumber - 1, 1));
    return date.toLocaleString('default', { month: 'long' });
}

function initYearInventoryChart(jsonData) {
    $("#inventoryActivityTitle").text("Inventory Activity Per Month");
    const data = jsonData;

    const monthlyTotals = {};

    data.forEach((item) => {
        const month = getMonthName(item.month.substring(0, 7));
        if (monthlyTotals.hasOwnProperty(month)) {
            monthlyTotals[month].total += item.total;
            monthlyTotals[month].quantity += item.item_quantity;
        } else {
            monthlyTotals[month] = {
                total: item.total,
                quantity: item.item_quantity
            };
        }
    });

    const months = Object.keys(monthlyTotals);
    const totalQuantities = Object.values(monthlyTotals).map((data) => data.quantity); // Use "quantity" as the data

    const ctx = document.getElementById('myChart').getContext('2d');

    if (monthlyCHART) {
        monthlyCHART.destroy();
    }

    monthlyCHART = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: months,
            datasets: [{
                label: 'Quantity', // Change the label for the vertical axis
                data: totalQuantities,
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Quantity' // Change the label for the vertical axis
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Month' // Add a label for the horizontal axis
                    }
                }
            }
        }
    });
}

/*function initMonthInventoryChart(jsonData) {
    console.log(jsonData);
    // Parse the JSON data

    // Extract the data from the JSON
    const chartData = Array(12).fill(0);
    jsonData.forEach(item => {
        const month = parseInt(Object.keys(item)[0]);
        const value = item[month];

        // Set the value for the month in the chartData array
        chartData[month - 1] = value;
    });

    // Get the canvas element
    var ctx = document.getElementById('myChart').getContext('2d');

    // Define the data for the chart
    var data = {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
        datasets: [{
            label: 'Number of activities per month',
            data: chartData,
            backgroundColor: 'rgba(58, 65, 111, 0.2)',
            borderColor: '#3A416F',
            pointBackgroundColor: '#3A416F',
            borderWidth: 1
        }]
    };

    // Define the options for the chart
    var options = {
        scales: {
            y: {
                beginAtZero: true
            }
        }
    };

    // Create the chart
    var myChart = new Chart(ctx, {
        type: 'line',
        data: data,
        options: options
    });

    myChart.update();
}*/


function loadNewGroup(event) {
    event.preventDefault();
    $('#addItemModal').modal('hide');
    $('#addVendorModal').modal('hide');
    $('#addGroupModal').modal('show');
}

function addItemGroup() {

    $('#addGroupForm').submit(function(event) {
        event.preventDefault();

        $('#addGroupModal').modal('hide');
        var formData = $(this).serialize();

        $.ajax({
            url: '/api/inv/addItemGroup',
            method: 'POST',
            data: formData,
            success: function(response) {
                console.log(response);
                $('#infoModal .modal-body').empty();
                $('#infoModal .modal-body').append("<p>" + response.response + "</p>");
                $('#infoModal').modal('show');
                $('#infoModal #confirmOk').click(function () {
                    getGroups().then(function (response) {
                        $.each(response, function (index, data) {
                            let option = $('<option value="' + data.groupName + '">' + data.groupName + '</option>');
                            $('#itemGroup').empty();
                            $('#itemGroup').append(option);
                        })
                    }).catch(function (error) {
                        console.log(error);
                    })
                })            },
            error: function(xhr, status, error) {
                console.log(error);
                $('#infoModal .modal-body').empty();
                $('#infoModal .modal-body').append("<p>An unexpected error occurred</p>");
                $('#infoModal').modal('show');
            }
        });
    });
}

function addRequest() {

    $('#addRequestForm').submit(function(event) {
        event.preventDefault();

        $('#addPaymentRequestModal').modal('hide');
        var formData = $(this).serialize();

        $.ajax({
            url: '/api/inv/addPaymentRequest',
            method: 'POST',
            data: formData,
            success: function (response) {
                console.log(response);
                $('#infoModal .modal-body').empty();
                $('#infoModal .modal-body').append("<p>" + response.response + "</p>");
                $('#infoModal').modal('show');
                $('#infoModal #confirmOk').click(function () {
                    window.location.reload();
                })
            },
            error: function (xhr, status, error) {
                console.log(error);
                $('#infoModal .modal-body').empty();
                $('#infoModal .modal-body').append("<p>An unexpected error occurred</p>");
                $('#infoModal').modal('show');
            }
        });
    });
}

function openItemsFragment() {
    $.get('items').done(function (fragment) {

        $('#output').replaceWith(fragment);
    })
}

function addRequestModalLaunch() {
    $('#addPaymentRequestModal').modal('show');
}

function addItemModalLaunch() {
    getGroups().then(function (response) {
        $.each(response, function (index, data) {
            let option = $('<option value="' + data.groupName + '">' + data.groupName + '</option>');
            $('#itemGroup').empty();
            $('#addItemModal #itemGroup').append(option);
        })
    }).catch(function (error) {
        console.log(error);
    })
    getVendors().then(function (response) {
        $.each(response, function (index, data) {
            let option = $('<option value="' + data.id + '">' + data.company + '</option>');
            $('#preferredVendor').empty();
            $('#addItemModal #preferredVendor').append(option);
        })
    }).catch(function (error) {
        console.log(error);
    })
    $('#addItemModal').modal('show');
    document.getElementById("uploadButton").addEventListener("click", function (event) {
        event.preventDefault(); // Prevent form submission
        console.log("clicked");
        document.getElementById("imageInput").click();
    });
}

function openAdjustmentFragment() {
    $.get('inventoryAdjustment').done(function (fragment) {
        $('#pageName').text("Items");
        $('#pageNameH6').text("Items");
        $.ajax({
            url: '/api/inv/getAllAdjustments', contentType: 'application/json'
        }).done(function (response) {
            $('#itemsAdjustmentTable').DataTable({
                responsive: true, "data": response, "columns": [{
                    "data": "item", "render": function (data, type, row, meta) {
                        return '<p class="text-xs text-secondary mb-0">' + data.itemName + '</p>';
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }, {
                    "data": "date", "render": function (data, type, row, meta) {
                        return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }, {
                    "data": "reason", "render": function (data, type, row, meta) {
                        return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }, {
                    "data": "description", "render": function (data, type, row, meta) {
                        return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }, {
                    "data": "adjustmentType", "render": function (data, type, row, meta) {
                        return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }, {
                    "data": "adjustedBy", "render": function (data, type, row, meta) {
                        return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }, {
                    "data": "", "render": function (data, type, row, meta) {
                        return `
                                          <button class="btn btn-link text-secondary mb-0" id="navbarDropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                          <i class="fa fa-ellipsis-v text-xs"></i>
                                            </button>
                                        <div class="dropdown-menu dropdown-menu-end me-sm-n4 me-n3" aria-labelledby="navbarDropdownMenuLink">
                                            <a onclick="viewBook('${row.code}')" class="dropdown-item" href="javascript:;">View Book</a>
                                            <a onclick="editBookModalLaunch('${row.code}')" class="dropdown-item" href="javascript:;">Edit Book</a>
                                            <a onclick="deleteBook('${row.code}')" class="dropdown-item" href="javascript:;">Delete Book</a>
                                            <a onclick="markAsLost('${row.code}')" class="dropdown-item" href="javascript:;">Mark as Available</a>
                                         </div>
                                            `;
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }]
            })
        }).fail(function () {
            console.log(fail);
        })
        $('#output').replaceWith(fragment);
    })
}

$(document).ready(function() {
    $('#adjustmentType').on('change', function() {
        let adjustmentDiv = $('#valueOrQuantity');
        let valueOrQuantity = $('#adjustmentType option:selected').val();
        console.log(valueOrQuantity);
        let quantity = "<div class=\"card w-100\" id=\"left\">\n" +
            "                                            <div class=\"card-body table-responsive\">\n" +
            "                                                <div class=\"input-group input-group-dynamic mb-3\">\n" +
            "                                                    <label class=\"form-label\">Quantity</label>\n" +
            "                                                    <input name=\"quantity\" class=\"multisteps-form__input form-control\" type=\"number\" />\n" +
            "                                                </div>\n" +
            "\n" +
            "                                                <div class=\"input-group input-group-dynamic mb-3\">\n" +
            "                                                    <label class=\"form-label\">New Quantity</label>\n" +
            "                                                    <input name=\"newQuantity\" class=\"multisteps-form__input form-control\" type=\"number\" />\n" +
            "                                                </div>\n" +
            "\n" +
            "                                                <div class=\"input-group input-group-dynamic mb-3\">\n" +
            "                                                    <label class=\"form-label\">Adjusted</label>\n" +
            "                                                    <input name=\"adjusted\" class=\"multisteps-form__input form-control\" type=\"number\" />\n" +
            "                                                </div>\n" +
            "                                            </div>\n" +
            "                                        </div>";

        let value = "<div class=\"card w-100\" id=\"left\">\n" +
            "                                            <div class=\"card-body table-responsive\">\n" +
            "                                                <div class=\"input-group input-group-dynamic mb-3\">\n" +
            "                                                    <label class=\"form-label\">Value</label>\n" +
            "                                                    <input name=\"value\" class=\"multisteps-form__input form-control\" type=\"number\" />\n" +
            "                                                </div>\n" +
            "\n" +
            "                                                <div class=\"input-group input-group-dynamic mb-3\">\n" +
            "                                                    <label class=\"form-label\">New Value</label>\n" +
            "                                                    <input name=\"newValue\" class=\"multisteps-form__input form-control\" type=\"number\" />\n" +
            "                                                </div>\n" +
            "\n" +
            "                                                <div class=\"input-group input-group-dynamic mb-3\">\n" +
            "                                                    <label class=\"form-label\">Adjusted</label>\n" +
            "                                                    <input name=\"adjusted\" class=\"multisteps-form__input form-control\" type=\"number\" />\n" +
            "                                                </div>\n" +
            "                                            </div>\n" +
            "                                        </div>";
        adjustmentDiv.clear();
        if (valueOrQuantity === "Value Adjustment") {
            adjustmentDiv.append(value);
        } else {
            adjustmentDiv.append(quantity);
        }
    })
})

function addAdjustmentModalLaunch() {
    getItems().then(function (response) {
        $.each(response, function (index, data) {
            let option = $('<option value="' + data.itemId + '">' + data.itemName + '</option>');
            $('#addAdjustmentModal #item').append(option);
        })
    }).catch(function (error) {
        console.log(error);
    })
    $('#addAdjustmentModal').modal('show');
}





function addVendorModalLaunch() {
    getGroups().then(function (response) {
        $.each(response, function (index, data) {
            let option = $('<option value="' + data.groupName + '">' + data.groupName + '</option>');
            $('#addVendorModal #itemGroup').append(option);
        })
    }).catch(function (error) {
        console.log(error);
    })
    $('#addVendorModal').modal('show');
}

function openRequisitionFragment() {
    $.get('requisition').done(function (fragment) {
        $('#pageName').text("Requisition");
        $('#pageNameH6').text("Requisition");
        $.ajax({
            url: '/api/inv/getAllRequisitions', contentType: 'application/json'
        }).done(function (response) {
            $('#requisitionTable').DataTable({
                responsive: true, "data": response, "columns": [{
                    "data": "requestedOn", "render": function (data, type, row, meta) {
                        return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }, {
                    "data": "items", "render": function (data, type, row, meta) {
                        return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }, {
                    "data": "department", "render": function (data, type, row, meta) {
                        return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }, {
                    "data": "requestedBy", "render": function (data, type, row, meta) {
                        return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }, {
                    "data": "cost", "render": function (data, type, row, meta) {
                        return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }, {
                    "data": "status",
                    "render": function (data, type, row, meta) {
                        var comparisonResult = '';
                        if (data === "Pending") {
                            comparisonResult = 'text-warning';
                            return '<p class="text-xs ' + comparisonResult + ' mb-0">' + data + '</p>';
                        } else if (data === 'Approved') {
                            comparisonResult = 'text-success';
                            return '<p class="text-xs ' + comparisonResult + ' mb-0">' + data + '</p>';
                        } else if (data === 'Rejected') {
                            comparisonResult = 'text-danger';
                            return '<p class="text-xs ' + comparisonResult + ' mb-0">' + data + '</p>';
                        } else if (data === 'Ordered') {
                            comparisonResult = 'text-blue';
                            return '<p class="text-xs ' + comparisonResult + ' mb-0">' + data + '</p>';
                        }
                    },
                    "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }, {
                    "data": "id", "render": function (data, type, row, meta) {
                        if (row.status === "Pending") {
                            return `
                                          <button class="btn btn-link text-secondary mb-0" id="navbarDropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                          <i class="fa fa-ellipsis-v text-xs"></i>
                                            </button>
                                        <div class="dropdown-menu dropdown-menu-end me-sm-n4 me-n3" aria-labelledby="navbarDropdownMenuLink">
                                            <a onclick="print('/api/inv/printRequisitionForm/${data}')" class="dropdown-item" href="javascript:;">Print</a>
                                            <a onclick="rejectRequisition('/api/inv/rejectRequisition/${data}')" class="dropdown-item" href="javascript:;">Reject</a>
                                            <a onclick="viewRequisition('${data}')" class="dropdown-item" href="javascript:;">View Details</a>
                                            <a onclick="approveRequisition('/api/inv/approveRequisition/${data}')" class="dropdown-item" href="javascript:;">Approve</a>
                                         </div>
                                            `;
                        } else if (row.status === 'Approved') {
                            return `
                                          <button class="btn btn-link text-secondary mb-0" id="navbarDropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                          <i class="fa fa-ellipsis-v text-xs"></i>
                                            </button>
                                        <div class="dropdown-menu dropdown-menu-end me-sm-n4 me-n3" aria-labelledby="navbarDropdownMenuLink">
                                            <a onclick="print('/api/inv/printRequisitionForm/${data}')" class="dropdown-item" href="javascript:;">Print</a>
                                            <a onclick="rejectRequisition('/api/inv/rejectRequisition/${data}')" class="dropdown-item" href="javascript:;">Reject</a>
                                            <a onclick="viewRequisition('${data}')" class="dropdown-item" href="javascript:;">View Details</a>
                                            <a onclick="createPurchaseOrder('${data}')" class="dropdown-item" href="javascript:;">Create Purchase Order</a>
                                            <a onclick="viewRequisition('${data}')" class="dropdown-item" href="javascript:;">Request for quotation</a>
                                         </div>
                                            `;
                        } else if (row.status === 'Rejected') {
                            return `
                                          <button class="btn btn-link text-secondary mb-0" id="navbarDropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                          <i class="fa fa-ellipsis-v text-xs"></i>
                                            </button>
                                        <div class="dropdown-menu dropdown-menu-end me-sm-n4 me-n3" aria-labelledby="navbarDropdownMenuLink">
                                            <a onclick="print('/api/inv/printRequisitionForm/${data}')" class="dropdown-item" href="javascript:;">Print</a>
                                         </div>
                                            `;
                        } else if (row.status === 'Ordered') {
                            return `
                                          <button class="btn btn-link text-secondary mb-0" id="navbarDropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                          <i class="fa fa-ellipsis-v text-xs"></i>
                                            </button>
                                        <div class="dropdown-menu dropdown-menu-end me-sm-n4 me-n3" aria-labelledby="navbarDropdownMenuLink">
                                            <a onclick="viewRequisition('${data}')" class="dropdown-item" href="javascript:;">View Details</a>
                                            <a onclick="print('/api/inv/printRequisitionForm/${data}')" class="dropdown-item" href="javascript:;">Print</a>
                                         </div>
                                            `;
                        }
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }]
            })
        }).fail(function () {
            console.log(fail);
        })
        $('#output').replaceWith(fragment);
    })
}

function openPurchaseOrdersFragment() {
    $.get('purchaseOrders').done(function (fragment) {
        $('#pageName').text("Purchase Orders");
        $('#pageNameH6').text("Purchase Orders");
        $.ajax({
            url: '/api/inv/getAllPurchaseOrders', contentType: 'application/json'
        }).done(function (response) {
            console.log(response);
            $('#purchaseOrdersTable').DataTable({
                responsive: true, "data": response, "columns": [{
                    "data": "createdOn", "render": function (data, type, row, meta) {
                        return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }, {
                    "data": "expectedDate", "render": function (data, type, row, meta) {
                        return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }, {
                    "data": "items", "render": function (data, type, row, meta) {
                        return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }, {
                    "data": "comments", "render": function (data, type, row, meta) {
                        return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }, {
                    "data": "cost", "render": function (data, type, row, meta) {
                        return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }, {
                    "data": "status", "render": function (data, type, row, meta) {
                        var comparisonResult = '';
                        if (data === "Fully Received") {
                            comparisonResult = 'text-success';
                            return '<p class="text-xs ' + comparisonResult + ' mb-0">' + data + '</p>';
                        } else if (data === 'Not Received') {
                            comparisonResult = 'text-warning';
                            return '<p class="text-xs ' + comparisonResult + ' mb-0">' + data + '</p>';
                        } else {
                            comparisonResult = 'text-danger';
                            return '<p class="text-xs ' + comparisonResult + ' mb-0">' + data + '</p>';
                        }
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }, {
                    "data": "id", "render": function (data, type, row, meta) {
                        if (row.status === "Fully Received") {
                            return `
                                          <button class="btn btn-link text-secondary mb-0" id="navbarDropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                          <i class="fa fa-ellipsis-v text-xs"></i>
                                            </button>
                                        <div class="dropdown-menu dropdown-menu-end me-sm-n4 me-n3" aria-labelledby="navbarDropdownMenuLink">
                                            <a onclick="viewPurchaseOrder('${data}')" class="dropdown-item" href="javascript:;">View Details</a>
                                            <a onclick="print('/api/inv/printPurchaseOrder/${data}')" class="dropdown-item" href="javascript:;">Print</a>
                                         </div>
                                            `;
                        } else if(row.status === "Not Received") {
                            return `
                                          <button class="btn btn-link text-secondary mb-0" id="navbarDropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                          <i class="fa fa-ellipsis-v text-xs"></i>
                                            </button>
                                        <div class="dropdown-menu dropdown-menu-end me-sm-n4 me-n3" aria-labelledby="navbarDropdownMenuLink">
                                            <a onclick="viewPurchaseOrder('${data}')" class="dropdown-item" href="javascript:;">View Details</a>
                                            <a onclick="print('/api/inv/printPurchaseOrder/${data}')" class="dropdown-item" href="javascript:;">Print</a>
                                            <a onclick="cancelPO('/api/inv/cancelPurchaseOrder/${data}')" class="dropdown-item" href="javascript:;">Cancel</a>
                                            <a onclick="openReceiveItems('receiveItems/${data}')" class="dropdown-item" href="javascript:;">Receive Items</a>
                                         </div>
                                            `;
                        }
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }]
            })
        }).fail(function () {
            console.log(fail);
        })
        $('#output').replaceWith(fragment);
    })
}

function openAssetManagementFragment() {
    $.get('assetManagement').done(function (fragment) {
        $('#pageName').text("Asset Management");
        $('#pageNameH6').text("Asset Management");
        $.ajax({
            url: '/api/inv/getAllAssets', contentType: 'application/json'
        }).done(function (response) {
            $('#assetsTable').DataTable({
                responsive: true, "data": response, "columns": [
                    {
                        "data": "serial", "render": function (data, type, row, meta) {
                            return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                        }, "createdCell": function (td, cellData, rowData, row, col) {
                            $(td).addClass('align-middle text-center text-sm');
                        }
                    }, {
                        "data": "name", "render": function (data, type, row, meta) {
                            return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                        }, "createdCell": function (td, cellData, rowData, row, col) {
                            $(td).addClass('align-middle text-center text-sm');
                        }
                    }, {
                        "data": "type", "render": function (data, type, row, meta) {
                            return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                        }, "createdCell": function (td, cellData, rowData, row, col) {
                            $(td).addClass('align-middle text-center text-sm');
                        }
                    }, {
                        "data": "description", "render": function (data, type, row, meta) {
                            return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                        }, "createdCell": function (td, cellData, rowData, row, col) {
                            $(td).addClass('align-middle text-center text-sm');
                        }
                    }, {
                        "data": "openingBalance", "render": function (data, type, row, meta) {
                            return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                        }, "createdCell": function (td, cellData, rowData, row, col) {
                            $(td).addClass('align-middle text-center text-sm');
                        }
                    }, {
                        "data": "status", "render": function (data, type, row, meta) {
                            return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                        }, "createdCell": function (td, cellData, rowData, row, col) {
                            $(td).addClass('align-middle text-center text-sm');
                        }
                    }, {
                        "data": "id", "render": function (data, type, row, meta) {
                            return `
                                          <button class="btn btn-link text-secondary mb-0" id="navbarDropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                          <i class="fa fa-ellipsis-v text-xs"></i>
                                            </button>
                                        <div class="dropdown-menu dropdown-menu-end me-sm-n4 me-n3" aria-labelledby="navbarDropdownMenuLink">
                                            <a onclick="viewAsset('${data}')" class="dropdown-item" href="javascript:;">View Details</a>
                                            <a onclick="deleteItem('/api/inv/undoLastDepreciation/${data}')" class="dropdown-item" href="javascript:;">Undo Last Depreviation</a>
                                            <a onclick="deleteItem('/api/inv/deleteAsset/${data}')" class="dropdown-item" href="javascript:;">Delete</a>
                                         </div>
                                            `;
                        }, "createdCell": function (td, cellData, rowData, row, col) {
                            $(td).addClass('align-middle text-center text-sm');
                        }
                    }]
            })
        }).fail(function () {
            console.log(fail);
        })
        $('#output').replaceWith(fragment);
    })
}

function addAssetManagementModalLaunch() {
    getGroups().then(function (response) {
        $.each(response, function (index, data) {
            let option = $('<option value="' + data.groupName + '">' + data.groupName + '</option>');
            $('#addAssetModal #itemGroup').append(option);
        })
    }).catch(function (error) {
        console.log(error);
    })
    $('#addAssetModal').modal('show');
}

$(document).ready(function () {
    const imageView = document.getElementById('imageView');
    const imageInput = document.getElementById('imageInput');
    imageInput.addEventListener('change', (event) => {
        console.log(imageInput);
        const file = event.target.files[0];
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
            const image = new Image();
            image.src = reader.result;
            $('#imageUrl').val(reader.result);
            console.log(reader.result);
            image.onload = () => {
                const width = image.width;
                const height = image.height;
                const aspectRatio = width / height;
                let newWidth = 300;
                let newHeight = 300;
                if (width > height) {
                    newHeight = newWidth / aspectRatio;
                } else {
                    newWidth = newHeight * aspectRatio;
                }
                imageView.style.backgroundImage = `url(${reader.result})`;
                imageView.style.backgroundSize = `${newWidth}px ${newHeight}px`;
            };
        };
        uploadFile(file);
    });

    function uploadFile(file) {
        console.log("upload");
        var myFormData = new FormData();
        myFormData.append('pictureFile', file);
        myFormData.append('morepath', "inventory_images");

        $.ajax({
            url: "/upload",
            type: "POST",
            data: myFormData,
            processData: false,
            contentType: false,
            cache: false,
            success: function (res) {
                console.log(res);
                var obj = JSON.parse(res);
                console.log(obj);
                document.getElementById("imageUrl").value = obj['path'];

                console.log(obj['querystatus']);

                $(function () {
                    $("#messageid").html(obj['querystatus']);
                    $('#myModal').modal('show');
                    setTimeout(function () {
                        $('#myModal').modal('hide');
                    }, 1500);
                });

            },
            error: function (err) {
                $(function () {
                    $("#change-me").html(err);
                    $('#myModalError').modal('show');
                });
            }
        });


    }
})

function addItem() {
    $('#addItemForm').submit(function(event) {
        // Prevent the form from submitting normally
        event.preventDefault();

        $('#addItemModal').modal('hide');
        // Get the form data
        var formData = $(this).serialize();

        // Send an AJAX request
        $.ajax({
            type: 'POST',
            url: '/api/inv/addItem',
            data: formData,
            success: function(response) {
                $('#infoModal .modal-body').empty();
                $('#infoModal .modal-body').append("<p>"+response.response+"</p>");
                $('#infoModal').modal('show');

                $('#infoModal #confirmOk').click(function () {
                    openItemsFragment();
                })
            },
            error: function(error) {
                $('#infoModal .modal-body').empty();
                $('#infoModal .modal-body').append("<p>"+error+"</p>");
                $('#infoModal').modal('show');
            }
        });
    });
}

function deleteItem(url) {
    $('#myModaldelete').modal('show');
    $('#myModaldelete #confirmdeletebutt').on('click', function() {
        $('#myModaldelete').modal('hide');
        $.ajax({
            url: url,
            method: 'GET',
            contentType: 'application/json'
        }).done(function (response) {
            $('#infoModal .modal-body').empty();
            $('#infoModal .modal-body').append(response.response);
            $('#infoModal').css('z-index', 10510);
            $('#infoModal').modal('show');
        }).fail(function(fail) {
            console.log(fail);
        })
    })
}

function approveRequisition(url) {
    $('#myModaldelete #delete-modal-body').empty();
    $('#myModaldelete #delete-modal-body').text("Do you wish to approve this requisition?");
    $('#myModaldelete #confirmdeletebutt').text("APPROVE");
    $('#myModaldelete').modal('show');
    $('#myModaldelete #confirmdeletebutt').on('click', function() {
        $('#myModaldelete').modal('hide');
        $.ajax({
            url: url,
            method: 'GET',
            contentType: 'application/json'
        }).done(function (response) {
            $('#infoModal .modal-body').empty();
            $('#infoModal .modal-body').append(response.response);
            $('#infoModal').css('z-index', 10510);
            $('#infoModal').modal('show');

            $('#infoModal #confirmOk').click(function () {
                window.location.reload();
            })
        }).fail(function(fail) {
            console.log(fail);
        })
    })
}

function rejectRequisition(url) {
    $('#myModaldelete #delete-modal-body').empty();
    $('#myModaldelete #delete-modal-body').text("Do you wish to reject this requisition?");
    $('#myModaldelete #confirmdeletebutt').text("REJECT");
    $('#myModaldelete').modal('show');
    $('#myModaldelete #confirmdeletebutt').on('click', function() {
        $('#myModaldelete').modal('hide');
        $.ajax({
            url: url,
            method: 'GET',
            contentType: 'application/json'
        }).done(function (response) {
            $('#infoModal .modal-body').empty();
            $('#infoModal .modal-body').append(response.response);
            $('#infoModal').css('z-index', 10510);
            $('#infoModal').modal('show');
            $('#infoModal #confirmOk').click(function () {
                window.location.reload();
            })
        }).fail(function(fail) {
            console.log(fail);
        })
    })
}

function cancelPO(url) {
    $('#myModaldelete #delete-modal-body').empty();
    $('#myModaldelete #delete-modal-body').text("Do you wish to cancel this purchase order?");
    $('#myModaldelete #confirmdeletebutt').text("CANCEL");
    $('#myModaldelete').modal('show');
    $('#myModaldelete #confirmdeletebutt').on('click', function() {
        $('#myModaldelete').modal('hide');
        $.ajax({
            url: url,
            method: 'GET',
            contentType: 'application/json'
        }).done(function (response) {
            $('#infoModal .modal-body').empty();
            $('#infoModal .modal-body').append(response.response);
            $('#infoModal').css('z-index', 10510);
            $('#infoModal').modal('show');
            $('#infoModal #confirmOk').click(function () {
                window.location.reload();
            })
        }).fail(function(fail) {
            console.log(fail);
        })
    })
}

function openBillsFragment() {
    $.get('bills').done(function (fragment) {
        $('#pageName').text("Bills");
        $('#pageNameH6').text("Bills");
        $.ajax({
            url: '/api/inv/getAllBills', contentType: 'application/json'
        }).done(function (response) {
            $('#billsTable').DataTable({
                responsive: true, "data": response, "columns": [
                    {
                        "data": "billdate", "render": function (data, type, row, meta) {
                            return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                        }, "createdCell": function (td, cellData, rowData, row, col) {
                            $(td).addClass('align-middle text-center text-sm');
                        }
                    }, {
                        "data": "name", "render": function (data, type, row, meta) {
                            return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                        }, "createdCell": function (td, cellData, rowData, row, col) {
                            $(td).addClass('align-middle text-center text-sm');
                        }
                    }, {
                        "data": "amt", "render": function (data, type, row, meta) {
                            return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                        }, "createdCell": function (td, cellData, rowData, row, col) {
                            $(td).addClass('align-middle text-center text-sm');
                        }
                    }, {
                        "data": "paid", "render": function (data, type, row, meta) {
                            return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                        }, "createdCell": function (td, cellData, rowData, row, col) {
                            $(td).addClass('align-middle text-center text-sm');
                        }
                    }, {
                        "data": "pending", "render": function (data, type, row, meta) {
                            return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                        }, "createdCell": function (td, cellData, rowData, row, col) {
                            $(td).addClass('align-middle text-center text-sm');
                        }
                    }, {
                        "data": "status", "render": function (data, type, row, meta) {
                            if (data === "Not Paid") {
                                $(row).css('background-color', 'red');
                            } else if (data === "Paid") {
                                $(row).css('background-color', 'green');
                            } else if (data === "Partially Paid") {
                                $(row).css('background-color', 'blue');
                            }
                            return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                        }, "createdCell": function (td, cellData, rowData, row, col) {
                            $(td).addClass('align-middle text-center text-sm');
                        }
                    }]
            })
        }).fail(function () {
            console.log(fail);
        })
        $('#output').replaceWith(fragment);
    })
}

function openRequestPaymentFragment() {
    $.get('requestPayment').done(function (fragment) {
        $('#pageName').text("Payment Requests");
        $('#pageNameH6').text("Payment Requests");
        $.ajax({
            url: '/api/inv/getAllPaymentRequests', contentType: 'application/json'
        }).done(function (response) {
            $('#paymentRequestsTable').DataTable({
                responsive: true, "data": response, "columns": [
                    {
                        "data": "createdOn", "render": function (data, type, row, meta) {
                            let date = new Date(data);
                            const formattedDate = date.toLocaleDateString();
                            const formattedTime = date.toLocaleTimeString();

                            let formatted = formattedDate + " " + formattedTime;
                            return '<p class="text-xs text-secondary mb-0">' + formatted + '</p>';
                        }, "createdCell": function (td, cellData, rowData, row, col) {
                            $(td).addClass('align-middle text-center text-sm');
                        }
                    }, {
                        "data": "amount", "render": function (data, type, row, meta) {
                            return '<p class="text-xs text-secondary mb-0">' + data.toLocaleString() + '</p>';
                        }, "createdCell": function (td, cellData, rowData, row, col) {
                            $(td).addClass('align-middle text-center text-sm');
                        }
                    }, {
                        "data": "details", "render": function (data, type, row, meta) {
                            return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                        }/*, "createdCell": function (td, cellData, rowData, row, col) {
                            $(td).addClass('align-middle text-center text-sm');
                        }*/
                    }, {
                        "data": "status", "render": function (data, type, row, meta) {
                            if (data === "Approved") {
                                $(row).css('background-color', 'green');
                            } else if (data === "Reviewed") {
                                $(row).css('background-color', 'blue');
                            } else if (data === "Pending") {
                                $(row).css('background-color', 'yellow');
                            }
                            return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                        }, "createdCell": function (td, cellData, rowData, row, col) {
                            $(td).addClass('align-middle text-center text-sm');
                        }
                    }]
            })
        }).fail(function () {
            console.log(fail);
        })
        $('#output').replaceWith(fragment);
    })
}

function oepnAddRequisitionFragment() {
    $.post('openAddRequisitionFragment').done(function (fragment) {
        $('#pageName').text("Add Requisition");
        $('#pageNameH6').text("Add Requisition");
        getItems().then(function (response) {
            $.each(response, function (index, data) {
                let option = $('<option value="' + data.itemId + ' - '+data.itemName+'">' + data.itemName + '</option>');
                $('#requisitionItem').append(option);
            })
        }).catch(function (error) {
            console.log(error);
        })
        $('#output').replaceWith(fragment);
        $('#pendingRequisitionTable').DataTable({
            responsive: true
        });
    })
}

function createPurchaseOrder(id) {
    $.post('addPurchaseOrder').done(function (fragment) {
        $('#pageName').text("New Purchase Order");
        $('#pageNameH6').text("New Purchase Order");

        getVendors().then(function (response) {
            $.each(response, function (index, data) {
                let option = $('<option value="' + data.id +'">' + data.company + '</option>');
                $('#selectVendor').append(option);
            })
        }).catch(function (error) {
            console.log(error);
        })
        $.ajax({
            url: '/api/inv/getApprovedRequisition/'+ id,
            method: 'GET',
            contentType: 'application/json'
        }).done(function(response) {
            $('#pendingPOTable').DataTable({
                responsive: true, "data": response, "columns": [
                    {
                    "data": "image", "render": function (data, type, row, meta) {
                        return '<div class="d-flex px-2 py-1">\n' + ' <div>\n' + ' <img src="' + data + '" class="avatar avatar-sm me-3 border-radius-lg" alt="">\n' + '</div>\n' + '</div>';
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }, {
                    "data": "itemName", "render": function (data, type, row, meta) {
                        return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }, {
                    "data": "details", "render": function (data, type, row, meta) {
                        return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }, {
                    "data": "price", "render": function (data, type, row, meta) {
                        return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }, {
                    "data": "quantity", "render": function (data, type, row, meta) {
                        return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }, {
                    "data": "total", "render": function (data, type, row, meta) {
                        return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                    }, "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).addClass('align-middle text-center text-sm');
                    }
                }]
            })
        }).fail(function(fail) {
            console.log(fail);
        })

        $('#output').replaceWith(fragment);
    })
}

function openReceiveItems(id) {
    $.post('receiveItems').done(function (fragment) {
        $('#pageName').text("Receive Items");
        $('#pageNameH6').text("Receive Items");

        $.ajax({
            url: '/api/inv/getPendingPO/'+ id,
            method: 'GET',
            contentType: 'application/json'
        }).done(function(response) {
            console.log(response)
            $('#vendor').text(response[0].company);
            $('#expectedOnView').text(response[0].expectedDate);
            $('#pendingReceiveTable').DataTable({
                responsive: true, "data": response, "columns": [
                    {
                        "data": "itemName", "render": function (data, type, row, meta) {
                            return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                        }, "createdCell": function (td, cellData, rowData, row, col) {
                            $(td).addClass('align-middle text-center text-sm');
                        }
                    }, {
                        "data": "quantity", "render": function (data, type, row, meta) {
                            return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                        }, "createdCell": function (td, cellData, rowData, row, col) {
                            $(td).addClass('align-middle text-center text-sm');
                        }
                    }, {
                        "data": "quantityReceived", "render": function (data, type, row, meta) {
                            return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                        }, "createdCell": function (td, cellData, rowData, row, col) {
                            $(td).addClass('align-middle text-center text-sm');
                        }
                    }, {
                        "data": "outstanding", "render": function (data, type, row, meta) {
                            return '<p class="text-xs text-secondary mb-0">' + data + '</p>';
                        }, "createdCell": function (td, cellData, rowData, row, col) {
                            $(td).addClass('align-middle text-center text-sm');
                        }
                    }, {
                        "data": "expectedDate", "render": function (data, type, row, meta) {
                            return '<p class="text-xs text-secondary mb-0"><input id="qtyReceiving" name="qtyReceiving" class="form-control" type="number" placeholder="Quantity Receiving" required></p>';
                        },
                        "createdCell": function (td, cellData, rowData, row, col) {
                            $(td).addClass('align-middle text-center text-sm');
                        }
                    }
                ]
            })
        }).fail(function(fail) {
            console.log(fail);
        })

        $('#output').replaceWith(fragment);
    })
}

function issueItems() {
    $.post('issueItems').done(function (fragment) {
        $('#pageName').text("Issue Items");
        $('#pageNameH6').text("Issue Items");
        $('#output').replaceWith(fragment);
        $('#pendingIssueTable').DataTable({
            responsive: true
        });
    })
}

function returnItems() {
    $.post('returnItems').done(function (fragment) {
        $('#pageName').text("Return Items");
        $('#pageNameH6').text("Return Items");
        $.ajax({
            url: '/api/inv/getIssuedItems',
            method: 'GET'
        }).done(function (result) {
            console.log(result);
            for (let i = 0; i < result.length; i++) {
                let arr = result[i];
                let obj1 = arr[0];
                let obj2 = arr[1];
                result[i] = Object.assign({}, obj1, obj2);
            }
            $('#returnItemsTable').DataTable({
                "data": result,
                "columns": [
                    {"data": "itemName"},
                    {"data": "quantity"},
                    {"data": "itemPrice"},
                    {"data": "issuedOn"},
                    {"data": "returnDate"},
                    {"data": "status"},
                    {"data": "individualId", "visible": false},
                ],
                "rowCallback": function(row, data) {
                    $(row).on("click", function() {
                        $('tr').css({
                            "background-color": "",
                            "color": ""
                        });
                        // Set background and text color of clicked row
                        $(row).css({
                            "background-color": "red",
                            "color": "white"
                        });
                        selectedIssuedItem = data;
                    });
                }
            })
        }).fail(function (error) {
            console.log(error);
        })


        $('#output').replaceWith(fragment);
    })
}

function returnTheItem() {
    let comment = $('#comment').val();
    let quantity = $('#quantity').val();

    let obj = {
        selectedIssuedItem: selectedIssuedItem,
        comment: comment,
        quantity: quantity
    }

    $.ajax({
        url: '/api/inv/returnIssuedItem',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(obj)
    }).done(function(response) {
        response = JSON.parse(response);
        console.log(response);
        $('#infoModal .modal-body').empty();
        $('#infoModal .modal-body').append("<p>"+response.response+"</p>");
        $('#infoModal').modal('show');

        $('#infoModal #confirmOk').click(function () {
            window.location.reload();
        })
    }).fail(function (error) {
        console.log(error);
        $('#infoModal .modal-body').empty();
        $('#infoModal .modal-body').append("<p>"+error.response+"</p>");
        $('#infoModal').modal('show');
    })
}

function searchItem() {
    var query = $('#searchTitle').val();
    console.log(query);
    $.post('/api/inv/searchItems', {query: query}, function(results) {
        console.log(results);
        var dropdown = $("#book .searchResults");
        if (query === "") {
            dropdown.empty();
            return;
        }
        dropdown.empty();
        for (var i = 0; i < results.length; i++) {
            var data = results[i].id + "-" + results[i].itemName;
            var option = $("<option>")
                .attr("value", results[i].id)
                .text(data)
                .click(function() {
                    $('#searchTitle').val($(this).text());
                    dropdown.empty();
                })
                .hover(function() {
                    $(this).addClass('highlighted');
                }, function() {
                    $(this).removeClass('highlighted');
                });
            dropdown.append(option);
        }
    });
}

function searchReturnBook() {
    var query = $('#searchTitle').val();
    $.post('/api/lib/searchReturnBooks', {query: query}, function(answer) {
        let results = JSON.parse(answer);
        console.log(results);
        var dropdown = $("#returnBook .searchResults");
        if (query === "") {
            dropdown.empty();
            return;
        }
        dropdown.empty();
        for (var i = 0; i < results.books.length; i++) {
            var data = results.books[i].code + " - " + results.books[i].title;
            console.log(data);
            var option = $("<option>")
                .attr("value", results.books[i].code)
                .text(data)
                .click(function() {
                    $('#searchTitle').val($(this).text());
                    dropdown.empty();
                })
                .hover(function() {
                    $(this).addClass('highlighted');
                }, function() {
                    $(this).removeClass('highlighted');
                });
            dropdown.append(option);
        }
    });
}


function searchPerson() {
    var query = $('#searchPerson').val();
    var recType = $('#recipientType').val();
    console.log(recType);
    let recTypeUrl = '/api/inv/searchRecipient';
    console.log(recTypeUrl);
    $.post(recTypeUrl, {query: query}, function(results) {
        console.log(results);
        var dropdown = $("#person .searchResults");
        if (query === "") {
            dropdown.empty();
            return;
        }
        dropdown.empty();
        for (var i = 0; i < results.length; i++) {
            var student = results[i].student;
            var option = $("<option>")
                .attr("value", results[i].admNo)
                .text(student)
                .click(function() {
                    $('#searchPerson').val($(this).text());
                    dropdown.empty()
                })
                .hover(function() {
                    $(this).addClass('highlighted');
                }, function() {
                    $(this).removeClass('highlighted');
                });
            dropdown.append(option);
        }
    });
}

function getItem(itemId) {
    return new Promise(function(resolve, reject) {
        $.ajax({
            url: '/api/inv/getItem/' + itemId,
            success: function (data) {
                resolve(data);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                reject(errorThrown);
            }
        });
    });
}

function addItemToRequisitionList() {
    var itemId = $('#requisitionItem').val().split(" - ")[0];
    var itemName = $('#requisitionItem').val().split(" - ")[1];
    var quantity = $('#quantity').val();
    var description = $('#description').val();
    getItem(itemId).then(function (item) {
        var table = $('#pendingRequisitionTable').DataTable();
        var total = quantity * item.itemPrice;
        table.row.add([
            null,
            itemId + " - " + item.itemName,
            description,
            item.itemPrice,
            quantity,
            total
        ]).draw();
        $('#itemId').val(itemId);
    }).catch(function (error) {
        console.log(error);
    });
    $('#quantity').val("");
    $('#description').val("");
}
function addItemToIssueList() {
    var itemId = $('#searchTitle').val().split("-")[0];
    console.log(itemId);
    var returnDate = $('#returnDate').val();
    var quantity = $('#quantity').val();

    getItem(itemId).then(function (item) {
        var table = $('#pendingIssueTable').DataTable();
        table.row.add([
            null,
            itemId + " - " + item.itemName,
            quantity,
            item.itemPrice,
            returnDate
        ]).draw();
        $('#itemId').val(itemId);
    }).catch(function (error) {
        console.log(error);
    });

    $('#searchTitle').val("");
}

function SaveIssue() {
    var recipient = $('#recipientId').val();
    var table = $('#pendingIssueTable').DataTable();
    var rowArray = table.rows().data().toArray();
    let rowObject = {};
    let array = [];

    for (let i = 0; i < rowArray.length; i++) {
        var itemId = $('#searchTitle').val().split("-")[0];
        rowObject = {
            imageUrl: rowArray[i][0],
            itemId: rowArray[i][1].split(" -")[0],
            item: rowArray[i][1].split("- ")[1],
            quantity: rowArray[i][2],
            date: rowArray[i][4]
        };

        array.push(rowObject);
    }

    console.log(array);

    var issue = {
        recipient: recipient,
        items: array
    }

    console.log(issue);

    $.ajax({
        url: '/api/inv/issueItem',
        contentType: 'application/json',
        method: 'POST',
        data: JSON.stringify(issue)
    }).done(function(response) {
        var message = JSON.parse(response);
        $('#infoModal .modal-body').empty();
        $('#infoModal .modal-body').append("<p>"+message.response+"</p>");
        $('#infoModal').modal('show');

        $('#infoModal #confirmOk').click(function () {
            window.location.reload();
        })
    }).fail(function(response) {
        var message = JSON.parse(response);
        $('#infoModal .modal-body').empty();
        $('#infoModal .modal-body').append("<p>"+message.response+"</p>");
        $('#infoModal').modal('show');
    })
}

function saveRequisition() {
    var department = $('#department').val();
    var requestedBy = $('#requestedBy').val();
    var table = $('#pendingRequisitionTable').DataTable();
    var rowArray = table.rows().data().toArray();
    let rowObject = {};
    let array = [];

    for (let i = 0; i < rowArray.length; i++) {
        rowObject = {
            itemId: rowArray[i][1].split(" -")[0],
            price: rowArray[i][3],
            quantity: rowArray[i][4],
            details: rowArray[i][2],
        };

        array.push(rowObject);
    }

    console.log(array);

    var issue = {
        department: department,
        requestedBy: requestedBy,
        items: array
    }

    console.log(issue);

    $.ajax({
        url: '/api/inv/addRequisition',
        contentType: 'application/json',
        method: 'POST',
        data: JSON.stringify(issue)
    }).done(function(response) {
        var message = JSON.parse(response);
        $('#infoModal .modal-body').empty();
        $('#infoModal .modal-body').append("<p>"+message.response+"</p>");
        $('#infoModal').modal('show');

        $('#infoModal #confirmOk').click(function () {
            window.location.href = "/requisition";
        })
    }).fail(function(response) {
        var message = JSON.parse(response);
        $('#infoModal .modal-body').empty();
        $('#infoModal .modal-body').append("<p>"+message.response+"</p>");
        $('#infoModal').modal('show');
    })
}

function savePurchaseOrder() {
    var vendor = $('#selectVendor').val();
    var expectedOn = $('#expectedOn').val();
    var comments = $('#comments').val();
    var table = $('#pendingPOTable').DataTable();
    var rowArray = table.rows().data().toArray();
    let rowObject = {};
    let array = [];
    for (let i = 0; i < rowArray.length; i++) {
        rowObject = {
            requisition_id: rowArray[i].id,
            itemId: rowArray[i].itemId,
            itemName: rowArray[i].itemName,
            price: rowArray[i].price,
            quantity: rowArray[i].quantity,
            details: rowArray[i].details,
        };

        array.push(rowObject);
    }

    console.log(array);

    var issue = {
        vendor: vendor,
        expectedOn: expectedOn,
        comments: comments,
        items: array
    }

    console.log(issue);

    $.ajax({
        url: '/api/inv/addPurchaseOrder',
        contentType: 'application/json',
        method: 'POST',
        data: JSON.stringify(issue)
    }).done(function(response) {
        var message = JSON.parse(response);
        $('#infoModal .modal-body').empty();
        $('#infoModal .modal-body').append("<p>"+message.response+"</p>");
        $('#infoModal').modal('show');

        $('#infoModal #confirmOk').click(function () {
            if (message.response === 'Purchase order made successfully') {
                window.location.href = "/purchaseOrders";
            }
        })
    }).fail(function(response) {
        var message = JSON.parse(response);
        $('#infoModal .modal-body').empty();
        $('#infoModal .modal-body').append("<p>"+message.response+"</p>");
        $('#infoModal').modal('show');
    })
}

function receiveItems() {
    var vendor = $('#vendor').val();
    var expectedOn = $('#expectedOnView').val();
    var table = $('#pendingReceiveTable').DataTable();
    var rows = table.rows().nodes();
    var array = [];
    $(rows).each(function() {
        var row = $(this);
        var qtyReceiving = row.find('input[name="qtyReceiving"]').val();

        var rowData = table.row(row).data();
        var itemId = rowData.itemId;
        var itemName = rowData.itemName;
        var quantity = rowData.quantity;
        var quantityReceived = rowData.quantityReceived;
        var outstanding = rowData.outstanding;
        var poId = rowData.poId;
        var poItemsId = rowData.poItemsId;

        var rowObject = {
            itemId: itemId,
            itemName: itemName,
            quantity: quantity,
            quantityReceived: quantityReceived,
            outstanding: outstanding,
            poId: poId,
            poItemsId: poItemsId,
            quantityReceiving: qtyReceiving
        };

        array.push(rowObject);
    });

    var issue = {
        vendor: vendor,
        expectedOn: expectedOn,
        items: array
    }

    console.log(issue);

    $.ajax({
        url: '/api/inv/receiveItems',
        contentType: 'application/json',
        method: 'POST',
        data: JSON.stringify(issue)
    }).done(function(response) {
        var message = JSON.parse(response);
        $('#infoModal .modal-body').empty();
        $('#infoModal .modal-body').append("<p>"+message.response+"</p>");
        $('#infoModal').modal('show');

        $('#infoModal #confirmOk').click(function () {
            if (message.response === 'Item(s) received successfully') {
                window.location.reload();
            }
        })
    }).fail(function(response) {
        var message = JSON.parse(response);
        $('#infoModal .modal-body').empty();
        $('#infoModal .modal-body').append("<p>"+message.response+"</p>");
        $('#infoModal').modal('show');
    })

}

function depreciateAll() {
    $('#myModaldelete').modal('show');
    $('#myModaldelete #confirmdeletebutt').on('click', function() {
        $('#myModaldelete').modal('hide');
        $.ajax({
            url: '/api/inv/depreciateAll',
            method: 'POST',
            contentType: 'application/json'
        }).done(function (response) {
            $('#infoModal .modal-body').empty();
            $('#infoModal .modal-body').append(response.response);
            $('#infoModal').css('z-index', 10510);
            $('#infoModal').modal('show');
        }).fail(function(fail) {
            console.log(fail);
        })
    })
}

/* ----*****-----Reports -----*****----*/

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
function print(url) {
    Swal.fire({
        title: "Generating report...",
        text: "Please wait",
        imageUrl: "/assets/img/ajax-loader.gif",
        showConfirmButton: false,
        allowOutsideClick: false
    });
    // Create a new XMLHttpRequest object
    var xhr = new XMLHttpRequest();

    // Define what happens when the request is successfully completed
    xhr.onload = function() {
        var sampleArr = base64ToArrayBuffer(xhr.responseText);
        var file = new Blob([sampleArr], {type: 'application/pdf'});
        var fileURL = URL.createObjectURL(file);
        window.open(fileURL);
        Swal.fire({
            title: "Finished!",
            showConfirmButton: false,
            timer: 1000
        });

        document.body.style.overflow = 'hidden';

    };

    // Send a GET request to the modal HTML file
    xhr.open('GET', url, true);
    xhr.send();
}

/* ----*****-----Reports -----*****----*/