let selectedIssuedBook = null;

function showupdatepasswindow() {
    $('#popupupdatepass').modal('show');
}

function printAllResources() {
    // Create a new XMLHttpRequest object
    var xhr = new XMLHttpRequest();

    // Define what happens when the request is successfully completed
    xhr.onload = function() {

        var sampleArr = base64ToArrayBuffer(xhr.responseText);
        var file = new Blob([sampleArr], {type: 'application/pdf'});
        var fileURL = URL.createObjectURL(file);
        window.open(fileURL);
        $("#modalprogress2").modal("hide");

        document.body.style.overflow = 'hidden';

    };

    // Send a GET request to the modal HTML file
    xhr.open('GET', '/api/lib/listOfResources', true);
    xhr.send();
}

function postupdatepass() {

    if ($('#newpass').val() != $('#confirmnewpass').val()) {
        $(function () {
            $('#infoModal .modal-body').empty();
            $('#infoModal .modal-body').append("Passwords do not match");
            $('#infoModal').modal('show');
            setTimeout(function () {
                $('#infoModal').modal('hide');
            }, 2000);
        });

        return;
    }else if ($('#newpass').val().length<5) {
        $(function () {
            $('#infoModal .modal-body').empty();
            $('#infoModal .modal-body').append("Password cannot be less than five characters");
            $('#infoModal').modal('show');
            setTimeout(function () {
                $('#infoModal').modal('hide');
            }, 2000);
        });

        return;
    }else if ($('#confirmnewpass').val().length<5) {
        $(function () {
            $('#infoModal .modal-body').empty();
            $('#infoModal .modal-body').append("Password cannot be less than five characters");
            $('#infoModal').modal('show');
            setTimeout(function () {
                $('#infoModal').modal('hide');
            }, 2000);
        });

        return;
    }else{


        $("#popupupdatepass").modal("hide");

        var indexed_array = {};

        indexed_array['oldpass'] = $("#oldpass").val();
        indexed_array['newpass'] = $("#newpass").val();

        $.ajax({

            type : "POST",
            url : '/api/settings/postupdatepass',
            contentType : "application/json",
            data : JSON.stringify(indexed_array),
            success: function(data){

                let response = JSON.parse(data)[0];
                $("#modalprogress2").modal("hide");

                $(function () {
                    $("#messageid").html(response['querystatus']);
                    $('#myModal').modal('show');
                    setTimeout(function () {
                        $('#myModal').modal('hide');
                    }, 2000);
                    $('#popupupdatepass').modal('hide');
                });




            }
        });


    }


}

function setActiveNav(clickedElement) {
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
    $(".arrow").click(function(e) {
        //let arrowParent = $(this).parent().parent();
        let arrowParent = $(this);
        arrowParent.toggleClass("showMenu");
    });

    let sidebar = $(".sidebar");
    let sidebarBtn = $(".bx-menu");
    sidebarBtn.click(function() {
        sidebar.toggleClass("close");
    });

})

function initLibraryChart(jsonData) {
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
}

$(document).ready(function() {
    $('.dataTable').each(function() {
        console.log("Disabling autocomplete for DataTables");
        $(this).find('.dataTables_filter input').attr('autocomplete', 'off');
    });
});


function openBooksManagement() {
    $.post("booksFragment").done(function (fragment) {
        $('#modalprogress2').modal('show');

        $('#pageName').text("Books Management");
        $('#pageNameH6').text("Books Management");
        $.ajax({
            url: '/api/lib/getAllBooks',
            success: function (data) {
                $(document).ready(function () {
                    $('#booksTable').DataTable({
                        "data": data,
                        "columns": [
                            {
                                "data": "image",
                                "render": function (data, type, row, meta) {
                                    return '<img src="' + data + '" width="50" height="50">';
                                }
                            },
                            { "data": "code", "className": "text-center" },
                            { "data": "title", "className": "text-center" },
                            { "data": "author", "className": "text-center" },
                            { "data": "category", "className": "text-center" },
                            { "data": "book_condition", "className": "text-center" },
                            {
                                "data": "status",
                                "render": function (data, type, row, meta) {
                                    if (data === "OUT") {
                                        return '<span style="vertical-align: center" class="badge badge-sm bg-gradient-secondary">' + data + '</span>';
                                    } else if (data === "LOST") {
                                        return '<span style="vertical-align: center" class="badge badge-sm bg-gradient-danger">' + data + '</span>';
                                    } else {
                                        return '<span class="badge badge-sm bg-gradient-success">' + data + '</span>';
                                    }
                                },
                                "className": "text-center"
                            },
                            {
                                "data": null,
                                "render": function (data, type, row, meta) {
                                    if (row.status === "LOST") {
                                        return `
                        <button class="btn btn-link text-secondary mb-0" id="navbarDropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <i class="fa fa-ellipsis-v text-xs"></i>
                        </button>
                        <div class="dropdown-menu dropdown-menu-end me-sm-n4 me-n3" aria-labelledby="navbarDropdownMenuLink">
                            <a onclick="viewBook('${row.code}')" class="dropdown-item" href="javascript:;">View Book</a>
                            <a onclick="editBookModalLaunch('${row.code}')" class="dropdown-item" href="javascript:;">Edit Book</a>
                            <a onclick="deleteBook('${row.code}')" class="dropdown-item" href="javascript:;">Delete Book</a>
                            <a onclick="markAsLost('${row.code}')" class="dropdown-item" href="javascript:;">Mark as Available</a>
                        </div>`;
                                    } else {
                                        return `
                        <button class="btn btn-link text-secondary mb-0" id="navbarDropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <i class="fa fa-ellipsis-v text-xs"></i>
                        </button>
                        <div class="dropdown-menu dropdown-menu-end me-sm-n4 me-n3" aria-labelledby="navbarDropdownMenuLink">
                            <a onclick="viewBook('${row.code}')" class="dropdown-item" href="javascript:;">View Book</a>
                            <a onclick="editBookModalLaunch('${row.code}')" class="dropdown-item" href="javascript:;">Edit Book</a>
                            <a onclick="deleteBook('${row.code}')" class="dropdown-item" href="javascript:;">Delete Book</a>
                            <a onclick="markAsLost('${row.code}')" class="dropdown-item" href="javascript:;">Mark as Lost</a>
                        </div>`;
                                    }
                                },
                                "className": "text-center"
                            }
                        ]
                    });
                });

            }
        });


        $("#output").replaceWith(fragment);
        document.body.style.zoom = '90%';
        setTimeout(function () {
            $('#modalprogress2').modal('hide');
        }, 1500);

    });
}

function openLibrarySettings() {
    $.ajax({
        url: '/api/lib/getAllLibrarySettings',
        success: function (data) {
            if (data.length > 0) {
                var librarySettings = data[0];
                var biometrics = librarySettings.use_biometrics;
                var fine = librarySettings.fine;
                var countFines = librarySettings.count_fines;
                var charge = librarySettings.charge_fines;
                var lostAfterDays = librarySettings.lost_book;

                var modal = $('#settingsModal');

                $(modal).find('#fine_amount').val(fine);
                $(modal).find('#days_lost').val(lostAfterDays);

                if (biometrics === 0) {
                    $('#noBiometrics').prop('checked', true);
                } else if (biometrics === 1) {
                    $('#yesBiometrics').prop('checked', true);
                }

                if (countFines === 0) {
                    $('#noCountFines').prop('checked', true);
                } else if (countFines === 1) {
                    $('#yesCountFines').prop('checked', true);
                }

                if (charge === 0) {
                    $('#noCalcAndCharge').prop('checked', true);
                } else if (charge === 1) {
                    $('#yesCalcAndCharge').prop('checked', true);
                }
            }
        }
    }).done(function () {
        $('#settingsModal').modal('show');
    });
}

$('#saveSettings').click(function () {
    var school = $('#school').val();
    var fine = $('#fine_amount').val();
    var days_lost = $('#days_lost').val();
    var biometrics, calcFines, countFinesLostBooks;

    if ($('#yesCalcAndCharge').is(":checked")) {
        calcFines = 1;
    } else {
        calcFines = 0;
    }

    if ($('#yesCountFines').is(":checked")) {
        countFinesLostBooks = 1;
    } else {
        countFinesLostBooks = 0;
    }

    if ($('#yesBiometrics').is(":checked")) {
        biometrics = 1;
    } else {
        biometrics = 0;
    }

    var settingsObject = {
        id: school,
        fine: fine,
        days_lost: days_lost,
        calcFines: calcFines,
        countFinesLostBooks: countFinesLostBooks,
        biometrics: biometrics
    }

    $.ajax({
        url: '/api/lib/addSettings',
        contentType: 'application/json',
        method: 'POST',
        data: JSON.stringify(settingsObject)
    }).done(function (response) {
        $('#settingsModal').modal('hide');

        var message = JSON.parse(response);
        $('#infoModal .modal-body').empty();
        $('#infoModal .modal-body').append("<p>"+message.response+"</p>");
        $('#infoModal').modal('show');
        $('#infoModal #confirmOk').click(function () {
            window.location.href = '/library';
        })

    }).fail(function (failure) {
        console.log(failure);
    })
})

$(document).ready(function() {
    $.ajax({
        url: '/api/lib/getAllLibrarySettings',
        contentType: 'application/json',
        method: 'GET'
    }).done(function (response) {
        if (response === null || response === "") {
            $('#settingsModal').modal('show');
        } else {
            $.ajax({
                url: '/api/lib/initDashboard',
                contentType: 'application/json',
                method: 'GET'
            }).done(function (response) {
                $('#titles').text(response.titles);
                $('#authors').text(response.authors);
                $('#publishers').html(parseInt(response.issued_not_returned).toLocaleString());
                $('#libraryValue').html(parseInt(response.price).toLocaleString());
                $('#totalBooks').text(parseInt(response.books).toLocaleString());
                $('#users').html(response.users);
                $('#totalFines').html(parseInt(response.fine).toLocaleString());
                $('#totalIssue').html(parseInt(response.total_issue).toLocaleString());
                var graph = response.graph;
                initLibraryChart(graph);

                initDefaultersTable();
            })
        }
    }).fail(function (fail) {
        console.log(fail);
    })
})

function initDefaultersTable() {
    $.ajax({
        url: '/api/lib/getAllFines',
        contentType: 'application/json'
    }).done(function(response) {
        $('#defaultersTable').DataTable({
            "data": response,
            "columns": [
                {"data": "admNo"},
                {
                    "data": null,
                    "render": function (data, type, row) {
                        return data.fName + ' ' + data.sName;
                    }
                },
                {"data": "code"},
                {"data": "title"},
                {"data": "daysBetween"}
            ]
        })
    }).fail(function(fail) {
        console.log(fail);
    })
}

function addBookModalLaunch() {
    $.ajax({
        url: '/api/lib/getAllForms',
        contentType: 'application/json'
    }).done(function (response) {
        $.each(response, function(index, classes) {
            let option = $('<option value="'+classes.classField+'">'+classes.classField+'</option>');
            $('#addBookModal #classes').append(option);
        })
    }).fail(function (fail) {
        console.log(fail);
    })
    $('#addBookModal').modal('show');
    document.getElementById("uploadButton").addEventListener("click", function(event) {
        event.preventDefault(); // Prevent form submission
        document.getElementById("imageInput").click();
    });
}

const imageView = document.getElementById('imageView');
const imageInput = document.getElementById('imageInput');
imageInput.addEventListener('change', (event) => {
    const file = event.target.files[0];
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
        const image = new Image();
        image.src = reader.result;
        $('#imageUrl').val(reader.result);
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

    var myFormData = new FormData();
    myFormData.append('pictureFile', file);
    myFormData.append('morepath', "library_images");

    $.ajax({
        url: "/upload",
        type: "POST",
        data: myFormData,
        processData: false,
        contentType: false,
        cache: false,
        success: function (res) {
            var obj = JSON.parse(res);
            document.getElementById("imageUrl").value = obj['path'];

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

var bulkReg = false;
$(document).ready(function() {
    $('#bulkReg').on('change', function() {
        if ($('#bulkReg').is(":checked")) {
            $('#bulkRegRow').css('display', 'flex');
            bulkReg = true;
        } else {
            $('#bulkRegRow').css('display', 'none');
            bulkReg = false;
        }
    })
})

$('#generateCodes').click(function () {
    var startCode, startNumber, year, copies;
    startCode = $('#start_code').val();
    startNumber = $('#start_number').val();
    year = $('#year').val();
    copies = $('#copies').val();

    var variables = {
        startCode: startCode,
        startNumber: startNumber,
        year: year,
        copies: copies
    }

    $.ajax({
        url: '/api/lib/generateCodes',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(variables)
    }).done(function (response) {
        var greenCodes = response.green_codes;
        $('#greenCodes').val(greenCodes.join(""));

        var redCodes = response.red_codes;
        $('#redCodes').val(redCodes.join(""));
    }).fail(function (response) {
        console.log(response);
    })
})

function addBook() {
    var code = $('[name="code"]').val();
    var title = $('[name="title"]').val();
    var author = $('[name="author"]').val();
    var publisher = $('[name="publisher"]').val();
    var price = $('[name="price"]').val();
    var category = $('[name="category"]').val();
    var classes = $('[name="classes"]').val();
    var condition = $('[name="condition"]').val();
    var subject = $('[name="subject"]').val();
    var other_notes = $('[name="other_notes"]').val();
    var imageInput = $('#imageUrl').val();

    var bookObject;

    if (bulkReg === true) {
        var bulkCodes = $('#greenCodes').val();
        bookObject = {
            code: bulkCodes,
            title: title,
            author: author,
            publisher: publisher,
            price: price,
            category: category,
            classes: classes,
            condition: condition,
            subject: subject,
            other_notes: other_notes,
            imageInput: imageInput,
            bulkReg: bulkReg
        };
    } else {
        bookObject = {
            code: code,
            title: title,
            author: author,
            publisher: publisher,
            price: price,
            category: category,
            classes: classes,
            condition: condition,
            subject: subject,
            other_notes: other_notes,
            imageInput: imageInput,
            bulkReg: bulkReg
        };
    }

    $.ajax({
        url: '/api/lib/addBook',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(bookObject)
    }).done(function (response) {
        $('#addBookModal').modal('hide');

        var message = JSON.parse(response);
        $('#infoModal .modal-body').empty();
        $('#infoModal .modal-body').append("<p>"+message.response+"</p>");
        $('#infoModal').modal('show');
        $('#infoModal #confirmOk').click(function () {
            openBooksManagement();
        })
    }).fail(function (response) {
        $('#infoModal .modal-body').empty();
        $('#infoModal .modal-body').append("<p>An unexpected error occurred</p>");
        $('#infoModal').modal('show');
    })
}

function editBook() {
    var code = $('#editBookModal input[name="code"]').val();
    var title = $('#editBookModal input[name="title"]').val();
    var author = $('#editBookModal input[name="author"]').val();
    var publisher = $('#editBookModal input[name="publisher"]').val();
    var price = $('#editBookModal input[name="price"]').val();
    var category = $('#editBookModal [name="category"]').val();
    var classes = $('#editBookModal [name="classes"]').val();
    var condition = $('#editBookModal [name="condition"]').val();
    var subject = $('#editBookModal input[name="subject"]').val();
    var other_notes = $('#editBookModal [name="other_notes"]').val();
    var imageInput = $('#imageUrl').val();

    var bookObject;

    bookObject = {
        code: code,
        title: title,
        author: author,
        publisher: publisher,
        price: price,
        category: category,
        classes: classes,
        condition: condition,
        subject: subject,
        other_notes: other_notes,
        imageInput: imageInput
    };

    $.ajax({
        url: '/api/lib/updateBook',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(bookObject)
    }).done(function (response) {
        $('#editBookModal').modal('hide');
        console.log(response);
        var message = JSON.parse(response);
        $('#infoModal .modal-body').empty();
        $('#infoModal .modal-body').append("<p>"+message.response+"</p>");
        $('#infoModal').modal('show');
        $('#infoModal #confirmOk').click(function () {
            openBooksManagement();
        })
    }).fail(function (response) {
        console.log(response);
        $('#infoModal .modal-body').empty();
        $('#infoModal .modal-body').append("<p>An unexpected error occurred</p>");
        $('#infoModal').modal('show');
    })
}

function issueBook() {
    $.post('issueBook').done(function (fragment) {
        $('#pageName').text("Issue Books");
        $('#pageNameH6').text("Issue Book");
        $('#output').replaceWith(fragment);
        $('#pendingIssueTable').DataTable({
            responsive: true
        });
    })
}

function returnBook() {
    $.post('returnBook').done(function (fragment) {
        $('#pageName').text("Return Books");
        $('#pageNameH6').text("Return Books");
        $.ajax({
            url: '/api/lib/getIssuedBooks',
            method: 'GET'
        }).done(function (result) {
            console.log(result);
            for (let i = 0; i < result.length; i++) {
                let arr = result[i];
                let obj1 = arr[0];
                let obj2 = arr[1];
                result[i] = Object.assign({}, obj1, obj2);
            }
            $('#returnBooksTable').DataTable({
                "data": result,
                "columns": [
                    {
                        "data": "image",
                        "render": function (data, type, row, meta) {
                            return '<img src="' + data + '" width="50" height="50">';
                        }
                    },
                    {"data": "code"},
                    {"data": "title"},
                    {"data": "condition"},
                    {"data": "returnDate"},
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
                        selectedIssuedBook = data;
                    });
                }
            })
        }).fail(function (error) {
            console.log(error);
        })


        $('#output').replaceWith(fragment);
    })
}

function returnTheBook() {
    console.log(selectedIssuedBook);
    let comment = $('#comment').val();
    let condition = $('#condition').val();

    let obj = {
        selectedIssuedBook: selectedIssuedBook,
        comment: comment,
        condition: condition
    }

    $.ajax({
        url: '/api/lib/returnIssuedBook',
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
            returnBook();
        })
    }).fail(function (error) {
        console.log(error);
        $('#infoModal .modal-body').empty();
        $('#infoModal .modal-body').append("<p>"+error.response+"</p>");
        $('#infoModal').modal('show');
    })
}

function openFines() {
    $.post('fines').done(function (fragment) {
        $('#pageName').text("Fines");
        $('#pageNameH6').text("Fines");
        $.ajax({
            url: '/api/lib/getAllFines'
        }).done(function (response) {
            $('#finesTable').DataTable({
                "data": response,
                "columns": [
                    {"data": "dateBorrowed"},
                    {"data": "daysBetween"},
                    {
                        "data": null,
                        "render": function (data, type, row) {
                            return data.admNo + ' - ' + data.fName + ' ' + data.sName;
                        }
                    },
                    {"data": "title"},
                    {"data": "fine"}
                ]
            })
        }).fail(function(error) {
            console.log(error);
        })

        $('#output').replaceWith(fragment);
    })
}

function getBook(unformattedCode) {
    let code = unformattedCode.replace(/\//g, '|');
    return new Promise(function(resolve, reject) {
        $.ajax({
            url: '/api/lib/getBook/' + code,
            success: function (data) {
                resolve(data);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                reject(errorThrown);
            }
        });
    });
}


function viewBook(unformattedCode) {
    let code = unformattedCode.replace(/\//g, '|');
    $.post('viewBook').done(function (fragment) {
        $('#pageName').text("View Book");
        $('#pageNameH6').text("View Book");
        getBook(code).then(function (data) {
            console.log(data);
            $('#code').append(data.code);
            $('#title').append(data.title);
            $('#author').append(data.author);
            $('#publisher').append(data.publisher);
            $('#category').append(data.category);
            $('#class').append(data.classes);
            $('#subject').append(data.subject);
            $('#book_condition').append(data.condition);
            $('#price').append(data.price);
            $('#notes').append(data.notes);
        }).catch(function (error) {
            console.log(error);
        })

        $.ajax({
            url: '/api/lib/getBookIssue/' + code,
            success: function (data) {
                console.log(data);
                $('#issuingListTable').DataTable({
                    "data": data,
                    "columns": [
                        {
                            "data": null,
                            "render": function (data, type, row) {
                                // Append the name to the individualId
                                return data.issuedBooks.individualId + " - " + data.name;
                            }
                        },
                        {
                            "data": null,
                            "render": function (data, type, row) {
                                // Append the name to the individualId
                                return data.issuedBooks.issuedOn;
                            }
                        },
                        {
                            "data": null,
                            "render": function (data, type, row) {
                                // Append the name to the individualId
                                return data.issuedBooks.returnedOn;
                            }
                        },
                        {
                            "data": null,
                            "render": function (data, type, row) {
                                // Append the name to the individualId
                                return data.issuedBooks.comments;
                            }
                        },
                    ]
                })
            }
        })


        $('#output').replaceWith(fragment);
    })
}

function searchBook() {
    var query = $('#searchTitle').val();
    $.post('/api/lib/searchBooks', {query: query}, function(results) {
        var dropdown = $("#book .searchResults");
        if (query === "") {
            dropdown.empty();
            return;
        }
        dropdown.empty();
        for (var i = 0; i < results.length; i++) {
            var data = results[i].code + "-" + results[i].title;
            var option = $("<option>")
                .attr("value", results[i].code)
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
    let recTypeUrl = "";
    if (recType === "Student") {
        recTypeUrl = '/api/lib/searchRecipient';
    } else if (recType === "Teacher") {
        recTypeUrl = '/api/lib/searchTeacher';
    } else if (recType === "Staff") {
        recTypeUrl = '/api/lib/searchStaff';
    }
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
            var teacher = results[i].teacher;
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
            var teacherOption = $("<option>")
                .attr("value", results[i].tscNo)
                .text(teacher)
                .click(function() {
                    $('#searchPerson').val($(this).text());
                    dropdown.empty()
                })
                .hover(function() {
                    $(this).addClass('highlighted-teacher');
                }, function() {
                    $(this).removeClass('highlighted-teacher');
                });
            dropdown.append(teacherOption);
        }
    });
}

function addBookToIssueList() {
    var code = $('#searchTitle').val().split("-")[0];
    var returnDate = $('#returnDate').val();

    getBook(code).then(function (book) {
        console.log(code)
        var table = $('#pendingIssueTable').DataTable();
        table.row.add([
            null,
            book.code,
            book.title,
            book.condition,
            returnDate
        ]).draw();
    }).catch(function (error) {
        console.log(error);
    });

    $('#searchTitle').val("");
}

function SaveIssue() {
    var recipient = $('#searchPerson').val();
    var recipientType = $('#recipientType').val();
    var table = $('#pendingIssueTable').DataTable();
    var rowArray = table.rows().data().toArray();
    let rowObject = {};
    let array = [];

    for (let i = 0; i < rowArray.length; i++) {
        rowObject = {
            imageUrl: rowArray[i][0],
            code: rowArray[i][1],
            status: rowArray[i][3],
            date: rowArray[i][4]
        };

        array.push(rowObject);
    }

    console.log(array);

    var issue = {
        recipient: recipient,
        recipientType: recipientType,
        books: array
    }

    console.log(issue);

    $.ajax({
        url: '/api/lib/issueBook',
        contentType: 'application/json',
        method: 'POST',
        data: JSON.stringify(issue)
    }).done(function(response) {
        var message = JSON.parse(response);
        $('#infoModal .modal-body').empty();
        $('#infoModal .modal-body').append("<p>"+message.response+"</p>");
        $('#infoModal').modal('show');

        $('#infoModal #confirmOk').click(function () {
            issueBook();
        })
    }).fail(function(response) {
        var message = JSON.parse(response);
        $('#infoModal .modal-body').empty();
        $('#infoModal .modal-body').append("<p>"+message.response+"</p>");
        $('#infoModal').modal('show');
    })
}

function editBookModalLaunch(code) {
    $.ajax({
        url: '/api/lib/getAllForms',
        contentType: 'application/json'
    }).done(function (response) {
        $.each(response, function(index, classes) {
            let option = $('<option value="'+classes.classField+'">'+classes.classField+'</option>');
            $('#editBookModal #classes').append(option);
        })
    }).fail(function (fail) {
        console.log(fail);
    })
    $('#editBookModal').modal('show');

    getBook(code).then(function (data) {
        $('#editBookModal input[name="code"]').val(data.code);
        $('#editBookModal input[name="title"]').val(data.title);
        $('#editBookModal input[name="author"]').val(data.author);
        $('#editBookModal input[name="publisher"]').val(data.publisher);
        $('#editBookModal [name="category"]').val(data.category);
        $('#editBookModal [name="classes"]').val(data.class);
        $('#editBookModal input[name="subject"]').val(data.subject);
        $('#editBookModal [name="book_condition"]').val(data.condition);
        $('#editBookModal input[name="price"]').val(data.price);
        $('#editBookModal input[name="notes"]').val(data.notes);
    }).catch(function (error) {
        console.log(error);
    })
}
$('.delete-modal').on('click', function (event) {
    event.preventDefault();
    var button = $(this);
    var url = button.data('link');
    var deleteModal = $('#myModaldelete');
    $(deleteModal).find('#confirmdeletebutt').on('click', function () {
        window.location = url;
    })
})

function deleteBook(unformattedCode) {
    $("#myModaldelete").modal('show');
    var deleteModal = $('#myModaldelete');
    $(deleteModal).find('#confirmdeletebutt').on('click', function () {
        let code = unformattedCode.replace(/\//g, '|');
        $.ajax({
            url: '/api/lib/book/delete/' + code
        }).done(function(response) {
            let message = JSON.parse(response);
            $('#infoModal .modal-body').empty();
            $('#infoModal .modal-body').append("<p>"+message.response+"</p>");
            $('#infoModal').modal('show');
            $('#infoModal #confirmOk').click(function () {
                openBooksManagement();
            })
        }).fail(function (error) {
            let message = JSON.parse(error);
            $('#infoModal .modal-body').empty();
            $('#infoModal .modal-body').append("<p>"+message.response+"</p>");
            $('#infoModal').modal('show');
        })
    })
}

function markAsLost(unformattedCode) {
    let code = unformattedCode.replace(/\//g, '|');
    $.ajax({
        url: '/api/lib/book/markLost/' + code
    }).done(function (response) {
        console.log(response);
        let message = JSON.parse(response);
        $('#infoModal .modal-body').empty();
        $('#infoModal .modal-body').append("<p>"+message.response+"</p>");
        $('#infoModal').modal('show');
        $('#infoModal #confirmOk').click(function () {
            openBooksManagement();
        })
    }).fail(function(error) {
        console.log(error);
        let message = JSON.parse(error);
        $('#infoModal .modal-body').empty();
        $('#infoModal .modal-body').append("<p>"+message.response+"</p>");
        $('#infoModal').modal('show');
    })
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
function printListOfResources() {
    // Create a new XMLHttpRequest object
    var xhr = new XMLHttpRequest();

    // Define what happens when the request is successfully completed
    xhr.onload = function() {

        var sampleArr = base64ToArrayBuffer(xhr.responseText);
        var file = new Blob([sampleArr], {type: 'application/pdf'});
        var fileURL = URL.createObjectURL(file);
        window.open(fileURL);
        $("#modalprogress2").modal("hide");

        document.body.style.overflow = 'hidden';

    };

    // Send a GET request to the modal HTML file
    xhr.open('GET', '/api/lib/listOfResources', true);
    xhr.send();
}

function printOverdueIssues() {
    // Create a new XMLHttpRequest object
    var xhr = new XMLHttpRequest();

    // Define what happens when the request is successfully completed
    xhr.onload = function() {

        var sampleArr = base64ToArrayBuffer(xhr.responseText);
        var file = new Blob([sampleArr], {type: 'application/pdf'});
        var fileURL = URL.createObjectURL(file);
        window.open(fileURL);
        $("#modalprogress2").modal("hide");

        document.body.style.overflow = 'hidden';

    };

    // Send a GET request to the modal HTML file
    xhr.open('GET', '/api/lib/overdueIssues', true);
    xhr.send();
}
function printLostResources() {
    // Create a new XMLHttpRequest object
    var xhr = new XMLHttpRequest();

    // Define what happens when the request is successfully completed
    xhr.onload = function() {

        var sampleArr = base64ToArrayBuffer(xhr.responseText);
        var file = new Blob([sampleArr], {type: 'application/pdf'});
        var fileURL = URL.createObjectURL(file);
        window.open(fileURL);
        $("#modalprogress2").modal("hide");

        document.body.style.overflow = 'hidden';

    };

    // Send a GET request to the modal HTML file
    xhr.open('GET', '/api/lib/lostResources', true);
    xhr.send();
}

function printLibraryActivity() {
    // Create a new XMLHttpRequest object
    var xhr = new XMLHttpRequest();

    // Define what happens when the request is successfully completed
    xhr.onload = function() {

        var sampleArr = base64ToArrayBuffer(xhr.responseText);
        var file = new Blob([sampleArr], {type: 'application/pdf'});
        var fileURL = URL.createObjectURL(file);
        window.open(fileURL);
        $("#modalprogress2").modal("hide");

        document.body.style.overflow = 'hidden';

    };

    // Send a GET request to the modal HTML file
    xhr.open('GET', '/api/lib/printLibraryActivity', true);
    xhr.send();
}

function printIssuesList() {
    // Create a new XMLHttpRequest object
    var xhr = new XMLHttpRequest();

    // Define what happens when the request is successfully completed
    xhr.onload = function() {

        var sampleArr = base64ToArrayBuffer(xhr.responseText);
        var file = new Blob([sampleArr], {type: 'application/pdf'});
        var fileURL = URL.createObjectURL(file);
        window.open(fileURL);
        $("#modalprogress2").modal("hide");

        document.body.style.overflow = 'hidden';

    };

    // Send a GET request to the modal HTML file
    xhr.open('GET', '/api/lib/printIssuesList', true);
    xhr.send();
}

function printFinesList() {
    // Create a new XMLHttpRequest object
    var xhr = new XMLHttpRequest();

    // Define what happens when the request is successfully completed
    xhr.onload = function() {

        var sampleArr = base64ToArrayBuffer(xhr.responseText);
        var file = new Blob([sampleArr], {type: 'application/pdf'});
        var fileURL = URL.createObjectURL(file);
        window.open(fileURL);
        $("#modalprogress2").modal("hide");

        document.body.style.overflow = 'hidden';

    };

    // Send a GET request to the modal HTML file
    xhr.open('GET', '/api/lib/printFinesList', true);
    xhr.send();
}