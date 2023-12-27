/* -- Check all checkboxes FX -- *//* -- Check all checkboxes FX -- */
var theme;
let type;
let isMember = false;
let baptised = false;
let confirmed = false;
let spouseBaptised = false;
let spouseConfirmed = false;
var searchitems = [];

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
var schoollogo;
var modaltoopen;
var uniqueid;
var actionsaveorupdate;
var uploadimagesoption;
var examid;
var collumns;
var rowperpage = 7;
var dataTableSearch;
var current = 1;


let myForm;

$('#allCheck').change(function () {
    if ($(this).prop('checked')) {
        var nodes = $(this).closest('table').DataTable().rows().nodes();

        $('input[type="checkbox"]', nodes).prop('checked', true);
    } else {
        var nodes = $(this).closest('table').DataTable().rows().nodes();

        $('input[type="checkbox"]', nodes).prop('checked', false);
    }
});

// Event listener for the table checkboxes
$(document).on('change', 'table input[type="checkbox"]', function () {
    var allCheck = $('#allCheck');
    var tableCheckboxes = $('table input[type="checkbox"]');
    if (!$(this).prop('checked')) {
        // If any table checkbox is unchecked, uncheck the allCheck checkbox
        allCheck.prop('checked', false);
    } else {
        // If all table checkboxes are checked, check the allCheck checkbox
        if (tableCheckboxes.filter(':not(:checked)').length === 0) {
            allCheck.prop('checked', true);
        }
    }
});

function clearInputFields(modal) {
    console.log(modal);
    $('#' + modal + ' input').val('');
}

function populateGroups(rowData) {
    return new Promise(function (resolve) {
        var optionsHtml = "";
        $.ajax({
            contentType: "application/json",
            url: "/finance/chart/groups/get/all",
            dataType: 'json',
            success: function (data) {
                $.each(data, function (index, option) {
                    if (rowData !== null) {
                        var selectedAttribute = (rowData.id === option.id) ? 'selected' : '';
                        optionsHtml += '<option value="' + option.id + '" ' + selectedAttribute + '>' + option.groupName + '</option>';
                    } else {
                        optionsHtml += '<option value="' + option.id + '">' + option.groupName + '</option>';

                    }
                });
                resolve(optionsHtml);
            }
        });
    });
}

function getMemberDetails(id) {
    return new Promise(function (resolve) {
        $.ajax({
            contentType: "application/json",
            url: "/api/member/get/" + id,
            dataType: 'json',
            success: function (data) {
                resolve(data);
            }
        });
    });
}

function populateMemberGroups(rowData) {
    return new Promise(function (resolve) {
        var optionsHtml = "";
        $.ajax({
            contentType: "application/json",
            url: "/api/member/groups/api",
            dataType: 'json',
            success: function (data) {
                $.each(data, function (index, option) {
                    if (rowData !== null) {
                        var selectedAttribute = (rowData === option.id) ? 'selected' : '';
                        optionsHtml += '<option value="' + option.id + '" ' + selectedAttribute + '>' + option.group + '</option>';
                    } else {
                        optionsHtml += '<option value="' + option.id + '">' + option.group + '</option>';

                    }
                });
                resolve(optionsHtml);
            }
        });
    });
}

function populateZones(rowData) {
    return new Promise(function (resolve) {
        var optionsHtml = "";
        $.ajax({
            contentType: "application/json",
            url: "/api/member/zones/api",
            dataType: 'json',
            success: function (data) {
                $.each(data, function (index, option) {
                    if (rowData !== null) {
                        var selectedAttribute = (rowData === option.id) ? 'selected' : '';
                        optionsHtml += '<option value="' + option.id + '" ' + selectedAttribute + '>' + option.zone + '</option>';
                    } else {
                        optionsHtml += '<option value="' + option.id + '">' + option.zone + '</option>';

                    }
                });
                resolve(optionsHtml);
            }
        });
    });
}

function populateChurchServices(rowData) {
    return new Promise(function (resolve) {
        var optionsHtml = "";
        $.ajax({
            contentType: "application/json",
            url: "/api/member/services/api",
            dataType: 'json',
            success: function (data) {
                $.each(data, function (index, option) {
                    if (rowData !== null) {
                        var selectedAttribute = (rowData === option.id) ? 'selected' : '';
                        optionsHtml += '<option value="' + option.id + '" ' + selectedAttribute + '>' + option.service + '</option>';
                    } else {
                        optionsHtml += '<option value="' + option.id + '">' + option.service + '</option>';

                    }
                });
                resolve(optionsHtml);
            }
        });
    });
}


function populateOptions(rowData) {
    return new Promise(function (resolve) {
        var optionsHtml = "";
        $.ajax({
            contentType: "application/json",
            url: "/finance/chart/get/all",
            dataType: 'json',
            success: function (data) {
                $.each(data, function (index, option) {
                    if (rowData !== null) {
                        var selectedAttribute = (rowData.activity === option.id) ? 'selected' : '';
                        optionsHtml += '<option value="' + option.id + '" ' + selectedAttribute + '>' + option.account1 + '</option>';
                    } else {
                        optionsHtml += '<option value="' + option.id + '">' + option.account1 + '</option>';

                    }
                });
                resolve(optionsHtml);
            }
        });
    });
}

function populateUsers(userId) {
    return new Promise(function (resolve) {
        var optionsHtml = "";
        $.ajax({
            contentType: "application/json",
            url: "/settings/api/users",
            dataType: 'json',
            success: function (data) {
                $.each(data, function (index, option) {
                    if (userId !== null) {
                        var selectedAttribute = (userId === option.id) ? 'selected' : '';
                        optionsHtml += '<option value="' + option.id + '" ' + selectedAttribute + '>' + option.username + '</option>';
                    } else {
                        optionsHtml += '<option value="' + option.id + '">' + option.username + '</option>';
                    }
                });
                resolve(optionsHtml);
            }
        });
    });
}

function openEditModal(model, id) {
    if (model === 'account') {
        $.ajax({
            url: '/finance/bank/get/' + id,
            contentType: 'application/json'
        }).done(function (accountData) {
            $('#editModal input[name="id"]').val(accountData.id);
            $('#editModal input[name="accountName"]').val(accountData.accountName).parent().addClass('is-filled');
            $('#editModal input[name="account"]').val(accountData.account).parent().addClass('is-filled');
            $('#editModal input[name="bankName"]').val(accountData.bankName).parent().addClass('is-filled');
            $('#editModal select[name="type"]').val(accountData.type);
            $('#editModal input[name="branch"]').val(accountData.branch).parent().addClass('is-filled');
            $('#editModal input[name="bankcode"]').val(accountData.bankcode).parent().addClass('is-filled');
            $('#editModal input[name="swiftcode"]').val(accountData.swiftcode).parent().addClass('is-filled');
        }).fail(function (response) {
            console.log(response);
        })
    } else if (model === 'voucher') {
        $('#editVouchersTable tbody').empty();
        $.ajax({
            url: '/finance/voucher/get/' + id,
            contentType: 'application/json'
        }).done(function (accountData) {
            console.log(accountData);
            let totalBeforeTax = 0;
            let withholding;
            let vat;
            let profFees;
            $.each(accountData, function (index, rowData) {
                totalBeforeTax += parseFloat(rowData.total);
                let row = '<tr>\n' +
                    '                                        <td>\n' +
                    '                                            <div class="input-group input-group-outline">\n' +
                    '                                                <input type="text" value="' + rowData.particulars + '" name="particulars" class="form-control">\n' +
                    '                                            </div>\n' +
                    '                                        </td>\n' +
                    '                                        <td>\n' +
                    '                                            <div class="input-group input-group-outline">\n' +
                    '                                                <input type="number" value="' + rowData.quantity + '" name="quantity" class="form-control quantity-input">\n' +
                    '                                            </div>\n' +
                    '                                        </td>\n' +
                    '                                        <td>\n' +
                    '                                            <div class="input-group input-group-outline">\n' +
                    '                                                <input type="number" value="' + rowData.rate + '" name="rate" class="form-control rate-input">\n' +
                    '                                            </div>\n' +
                    '                                        </td>\n' +
                    '                                        <td>\n' +
                    '                                            <div class="input-group input-group-outline">\n' +
                    '                                                <input type="number" value="' + rowData.total + '" name="amount" class="form-control amount-input" readonly>\n' +
                    '                                            </div>\n' +
                    '                                        </td>\n' +
                    '    <td>\n' +
                    '        <a class="btn btn-danger" onclick="deleteThisRow(this)"> <i class="fa fa-trash" aria-hidden="true"></i> </a>\n' +
                    '    </td>\n' +
                    '                                    </tr>';
                const newRow = $(row); // Convert the string to a jQuery object
                $('#editVouchersTable tbody').append(newRow);
                newRow.find(".quantity-input, .rate-input").on("input", function () {
                    var row = $(this).closest("tr");
                    updateAmount(row);
                    updateTotalAmountEdit();
                });

                $("#editModal #editBeforeTax").text("Before tax: " + totalBeforeTax.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,'));
                $('#editModal input[name="date"]').val(rowData.date).parent().addClass('is-filled');
                $('#editModal input[name="payee"]').val(rowData.payeeName).parent().addClass('is-filled');
                $('#editModal input[name="details"]').val(rowData.details).parent().addClass('is-filled');
                $('#editModal input[name="withholding"]').val(rowData.withholdingTax).parent().addClass('is-filled');
                $('#editModal input[name="vat"]').val(rowData.vatTax).parent().addClass('is-filled');
                $('#editModal input[name="profFees"]').val(rowData.professionalFees).parent().addClass('is-filled');
                $('#editModal select[name="activity"]').val(rowData.account);
                $('#editModal select[name="bank"]').val(rowData.activity);
                $('#editModal input[name="id"]').val(rowData.pvId);
            });


            withholding = parseFloat($('#editModal input[name="withholding"]').val());
            vat = parseFloat($('#editModal input[name="vat"]').val());
            profFees = parseFloat($('#editModal input[name="profFees"]').val());
            let payableAmount = totalBeforeTax - (((withholding / 100) * totalBeforeTax) + ((vat / 100) * totalBeforeTax) + ((profFees / 100) * totalBeforeTax));
            $('#editTotalPayable').text("Total Payable: " + payableAmount.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,'));
            localStorage.setItem("totalPayable", payableAmount);
        }).fail(function (response) {
            console.log(response);
        })
    } else if (model === 'receipt') {
        $('#editReceiptsTable tbody').empty();
        $.ajax({
            url: '/finance/receipt/get/' + id,
            contentType: 'application/json'
        }).done(function (accountData) {
            console.log(accountData);
            let payableAmount = 0;
            $('#editModal #editDate').val(accountData.date).parent().addClass('is-filled');
            $('#editModal input[name="payee"]').val(accountData.receivedFrom).parent().addClass('is-filled');
            $('#editModal input[name="details"]').val(accountData.details).parent().addClass('is-filled');
            $('#editModal input[name="transRef"]').val(accountData.transRef).parent().addClass('is-filled');
            /*$('#editModal select[name="activity"]').val(accountData.account);*/
            $('#editModal select[name="bank"]').val(accountData.bank);
            $('#editModal input[name="id"]').val(accountData.referenceNumber);
            $.each(accountData.receiptTableRows, async function (index, rowData) {
                payableAmount += rowData.amount;
                var optionsHtml = await populateOptions(rowData);

                let row = '<tr>\n' +
                    '    <td>\n' +
                    '        <input type="hidden" name="transactionId" value="' + rowData.transactionId + '">\n' +
                    '        <div class="input-group input-group-outline">\n' +
                    '            <select name="particulars" class="form-select form-dark-select">' + optionsHtml + '</select>\n' +
                    '        </div>\n' +
                    '    </td>\n' +
                    '    <td>\n' +
                    '        <div class="input-group input-group-outline">\n' +
                    '            <input type="text" value="' + rowData.amount + '" name="amount" class="form-control amount-input">\n' +
                    '        </div>\n' +
                    '    </td>\n' +
                    '    <td>\n' +
                    '        <a class="btn btn-danger" onclick="deleteThisRow(this)"> <i class="fa fa-trash" aria-hidden="true"></i> </a>\n' +
                    '    </td>\n' +
                    '</tr>';
                $('#editReceiptsTable tbody').append(row);
            });


            $('#editTotalPayable').text("Total Payable: " + payableAmount.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,'));
        }).fail(function (response) {
            console.log(response);
        })
    } else if (model === 'transaction') {
        $.ajax({
            url: '/finance/banking/transaction/get/' + id,
            contentType: 'application/json'
        }).done(function (result) {
            let response = result.accounttransaction;
            let bank = result.bankaccount;
            $('#editModal input[name="id"]').val(response.id);
            $('#editModal input[name="date"]').val(response.date).parent().addClass('is-filled');
            $('#editModal select[name="bank"]').val(bank.id);
            $('#editModal input[name="payeePayer"]').val(response.payeePayer).parent().addClass('is-filled');
            $('#editModal input[name="description"]').val(response.description).parent().addClass('is-filled');
            $('#editModal input[name="transRef"]').val(response.cheque).parent().addClass('is-filled');
            $('#editModal input[name="credit"]').val(response.credit).parent().addClass('is-filled');
            $('#editModal input[name="debit"]').val(response.debit).parent().addClass('is-filled');
        }).fail(function (response) {
            console.log(response);
        })
    } else if (model === 'memberGroups') {
        $.ajax({
            url: '/api/member/group/get/' + id,
            contentType: 'application/json'
        }).done(function (response) {
            $('#editModal input[name="id"]').val(response.id);
            $('#editModal input[name="group"]').val(response.group).parent().addClass('is-filled');
        }).fail(function (response) {
            console.log(response);
        })
    } else if (model === 'attendance') {
        $.ajax({
            url: '/api/member/attendance/get/' + id,
            contentType: 'application/json'
        }).done(function (response) {
            $('#editModal input[name="id"]').val(response.id);
            $('#editModal input[name="date"]').val(response.date).parent().addClass('is-filled');
            $('#editModal input[name="number"]').val(response.number).parent().addClass('is-filled');
            $('#editModal select[name="service"]').val(response.service.id).parent().addClass('is-filled');
        }).fail(function (response) {
            console.log(response);
        })
    } else if (model === 'services') {
        $.ajax({
            url: '/api/member/service/get/' + id,
            contentType: 'application/json'
        }).done(function (response) {
            $('#editModal input[name="id"]').val(response.id);
            $('#editModal input[name="service"]').val(response.service).parent().addClass('is-filled');
        }).fail(function (response) {
            console.log(response);
        })
    } else if (model === 'activityGroups') {
        $.ajax({
            url: '/finance/group/get/' + id,
            contentType: 'application/json'
        }).done(function (response) {
            $('#editModal input[name="id"]').val(response.id);
            $('#editModal input[name="activityGroup"]').val(response.group).parent().addClass('is-filled');
        }).fail(function (response) {
            console.log(response);
        })
    } else if (model === 'zones') {
        $.ajax({
            url: '/api/member/zone/get/' + id,
            contentType: 'application/json'
        }).done(function (response) {
            $('#editModal input[name="id"]').val(response.id);
            $('#editModal input[name="zone"]').val(response.zone).parent().addClass('is-filled');
        }).fail(function (response) {
            console.log(response);
        })
    } else if (model === 'talents') {
        $.ajax({
            url: '/api/member/talent/get/' + id,
            contentType: 'application/json'
        }).done(function (response) {
            $('#editModal input[name="id"]').val(response.id);
            $('#editModal input[name="talent"]').val(response.talent).parent().addClass('is-filled');
        }).fail(function (response) {
            console.log(response);
        })
    } else if (model === 'committees') {
        $.ajax({
            url: '/api/member/committee/get/' + id,
            contentType: 'application/json'
        }).done(function (response) {
            $('#editModal input[name="id"]').val(response.id);
            $('#editModal input[name="committee"]').val(response.committee).parent().addClass('is-filled');
        }).fail(function (response) {
            console.log(response);
        })
    } else if (model === 'departments') {
        $.ajax({
            url: '/api/member/department/get/' + id,
            contentType: 'application/json'
        }).done(function (response) {
            $('#editModal input[name="id"]').val(response.id);
            $('#editModal input[name="committee"]').val(response.committee).parent().addClass('is-filled');
        }).fail(function (response) {
            console.log(response);
        })
    } else if (model === 'users') {
        $.ajax({
            url: '/settings/user/get/' + id,
            contentType: 'application/json'
        }).done(function (response) {
            $('#editModal input[name="id"]').val(response.id);
            $('#editModal input[name="username"]').val(response.username).parent().addClass('is-filled');
            $('#editModal input[name="email"]').val(response.email).parent().addClass('is-filled');
        }).fail(function (response) {
            console.log(response);
        })
    } else if (model === 'activities') {
        $.ajax({
            url: '/finance/account/get/' + id,
            contentType: 'application/json'
        }).done(function (response) {
            $('#editModal input[name="id"]').val(response.id);
            $('#editModal input[name="account1"]').val(response.account1).parent().addClass('is-filled');
            $('#editModal select[name="accountgroup"]').val(response.accountgroup.id);
        }).fail(function (response) {
            console.log(response);
        })
    } else if (model === 'members') {
        myForm = '#employeesformedit';
        setProgressBar(1, 'default', '', '', '', myForm);
        $.ajax({
            url: 'api/member/get/' + id,
            contentType: 'application/json'
        }).done(function (response) {
            $('#editModal input[name="id"]').val(response.member.id);
            $('#editModal input[name="fName"]').val(response.member.fName).parent().addClass('is-filled');
            $('#editModal input[name="mName"]').val(response.member.mName).parent().addClass('is-filled');
            $('#editModal input[name="lName"]').val(response.member.lName).parent().addClass('is-filled');
            $('#editModal input[name="email"]').val(response.member.email).parent().addClass('is-filled');
            $('#editModal input[name="dob"]').val(response.member.dob).parent().addClass('is-filled');
            $('#editModal input[name="address"]').val(response.member.address).parent().addClass('is-filled');
            $('#editModal input[name="residence"]').val(response.member.residence).parent().addClass('is-filled');
            $('#editModal input[name="profession"]').val(response.member.profession).parent().addClass('is-filled');
            $('#editModal input[name="occupation"]').val(response.member.occupation).parent().addClass('is-filled');
            $('#editModal input[name="employer"]').val(response.member.employer).parent().addClass('is-filled');
            $('#editModal select[name="zone"]').val(response.member.zone.id);
            $('#editModal select[name="maritalStatus"]').val(response.member.maritalStatus);
            $('#editModal select[name="gender"]').val(response.member.gender);
            $('#editModal select[name="preferredService"]').val(response.member.preferredService.id);
            $('#editModal select[name="membershipDuration"]').val(response.member.membershipDuration.id);
            $('#editModal input[name="phone"]').val(response.member.phone).parent().addClass('is-filled');
            if (response.member.image !== "") {
                $('#editModal #imageedit').attr('src', response.member.image);
                $('#editModal #imageUrledit').val(response.member.image);
            }

            if (response.member.memberGroup !== null) {
                $('#flexSwitchCheckDefaultEdit').prop('checked', true);
                $('#memberGroupDivEdit').removeClass('hidden');
                $('#editModal select[name="memberGroup"]').val(response.member.memberGroup.id);
                $('#editModal input[name="enrolmentDate"]').val(response.member.enrolmentDate).parent().addClass('is-filled');
                $('#editModal input[name="enrolmentPlace"]').val(response.member.enrolmentPlace).parent().addClass('is-filled');
            }

            if (response.member.baptismStatus === 'true') {
                $('#baptisededit').prop('checked', true);
                $('#baptismDivedit').removeClass('hidden');
                $('#editModal input[name="baptismDate"]').val(response.member.baptismDetails.baptismDate).parent().addClass('is-filled');
                $('#editModal input[name="baptismPlace"]').val(response.member.baptismDetails.baptismPlace).parent().addClass('is-filled');
                $('#editModal input[name="baptisedBy"]').val(response.member.baptismDetails.baptisedBy).parent().addClass('is-filled');
            }

            if (response.member.confirmationStatus === 'true') {
                $('#confirmededit').prop('checked', true);
                $('#confirmationDivedit').removeClass('hidden');
                $('#editModal input[name="confirmationDate"]').val(response.member.confirmationDetails.confirmationDate).parent().addClass('is-filled');
                $('#editModal input[name="confirmationPlace"]').val(response.member.confirmationDetails.confirmationPlace).parent().addClass('is-filled');
                $('#editModal input[name="confirmedBy"]').val(response.member.confirmationDetails.confirmedBy).parent().addClass('is-filled');
            }

            if (response.member.maritalStatus === 'Married') {
                $('#spouseDetailsedit').removeClass('hidden');
                $('#deceasedSpouseDetails').addClass('hidden');
                $('#spouseId').val(response.spouse.id);
                $('#editModal input[name="weddingDate"]').val(response.spouse.marriage.weddingDate).parent().addClass('is-filled');
                $('#editModal input[name="weddingPlace"]').val(response.spouse.marriage.weddingPlace).parent().addClass('is-filled');
                $('#editModal select[name="marriageType"]').val(response.spouse.marriage.marriageType).parent().addClass('is-filled');


                $('#editModal input[name="spouseId"]').val(response.spouse.id);
                $('#editModal input[name="spouseFName"]').val(response.spouse.fName).parent().addClass('is-filled');
                $('#editModal input[name="spouseMName"]').val(response.spouse.mName).parent().addClass('is-filled');
                $('#editModal input[name="spouseLName"]').val(response.spouse.lName).parent().addClass('is-filled');
                $('#editModal input[name="spouseEmail"]').val(response.spouse.email).parent().addClass('is-filled');
                $('#editModal input[name="spouseDob"]').val(response.spouse.dob).parent().addClass('is-filled');
                $('#editModal input[name="spouseAddress"]').val(response.spouse.address).parent().addClass('is-filled');
                $('#editModal input[name="spouseResidence"]').val(response.spouse.residence).parent().addClass('is-filled');
                $('#editModal input[name="spouseProfession"]').val(response.spouse.profession).parent().addClass('is-filled');
                $('#editModal input[name="spouseOccupation"]').val(response.spouse.occupation).parent().addClass('is-filled');
                $('#editModal input[name="spouseEmployer"]').val(response.spouse.employer).parent().addClass('is-filled');
                $('#editModal select[name="spouseZone"]').val(response.spouse.zone.id);
                $('#editModal select[name="spouseMemberGroup"]').val(response.spouse.memberGroup.id);
                $('#editModal select[name="spouseMaritalStatus"]').val(response.spouse.maritalStatus);
                $('#editModal select[name="spouseGender"]').val(response.spouse.gender);
                $('#editModal select[name="spousePreferredService"]').val(response.spouse.preferredService.id);
                $('#editModal input[name="spousePhone"]').val(response.spouse.phone).parent().addClass('is-filled');
                if (response.spouse.image !== "") {
                    $('#editModal #spouseImageedit').attr('src', response.spouse.image);
                    $('#editModal #spouseImageUrledit').val(response.spouse.image);
                }

                if (response.spouse.baptismStatus === 'true') {
                    $('#spouseBaptisededit').prop('checked', true);
                    $('#spouseBaptismDivedit').removeClass('hidden');
                    $('#editModal input[name="spouseBaptismDate"]').val(response.spouse.baptismDetails.baptismDate).parent().addClass('is-filled');
                    $('#editModal input[name="spouseBaptismPlace"]').val(response.spouse.baptismDetails.baptismPlace).parent().addClass('is-filled');
                    $('#editModal input[name="spouseBaptisedBy"]').val(response.spouse.baptismDetails.baptisedBy).parent().addClass('is-filled');
                }

                if (response.spouse.confirmationStatus === 'true') {
                    $('#spouseConfirmededit').prop('checked', true);
                    $('#spouseConfirmationDivedit').removeClass('hidden');
                    $('#editModal input[name="spouseConfirmationDate"]').val(response.spouse.confirmationDetails.confirmationDate).parent().addClass('is-filled');
                    $('#editModal input[name="spouseConfirmationPlace"]').val(response.spouse.confirmationDetails.confirmationPlace).parent().addClass('is-filled');
                    $('#editModal input[name="spouseConfirmedBy"]').val(response.spouse.confirmationDetails.confirmedBy).parent().addClass('is-filled');
                }
            } else if (response.member.maritalStatus === 'Widow' || response.member.maritalStatus === 'Widower') {
                $('#deceasedSpouseDetailsEdit').removeClass('hidden');
                $('#spouseDetailsedit').addClass('hidden');
                $('#spouseId').val(response.spouse.id);
                $('#editModal input[name="spouseFName"]').val(response.spouse.fName).parent().addClass('is-filled');
                $('#editModal input[name="spouseMName"]').val(response.spouse.mName).parent().addClass('is-filled');
                $('#editModal input[name="spouseLName"]').val(response.spouse.lName).parent().addClass('is-filled');
            } else {
                $('#deceasedSpouseDetails').addClass('hidden');
                $('#spouseDetailsedit').addClass('hidden');
            }

            if (response.member.gender === 'Male') {
                $('#editModal input[name="numberOfChildren"]').val(response.childrenAsFather.length).parent().addClass('is-filled');
            } else {
                $('#editModal input[name="numberOfChildren"]').val(response.childrenAsMother.length).parent().addClass('is-filled');
            }
            if (response.childrenAsFather.length > 0) {
                $.each(response.childrenAsFather, async function (position, child) {

                    let i = position + 1;

                    $('#childDetailsedit').removeClass('hidden');
                    var membrGroupsOptions = await populateMemberGroups(child.memberGroup.id);
                    var zonesOptions = await populateZones(child.zone.id);
                    var servicesOptions = await populateChurchServices(child.preferredService.id);
                    let childOption = "<div id=\"toggleChild" + i + "Details\"\n" +
                        "                                             class=\"card-header p-0 position-relative mt-n4 mx-3 z-index-2 mt-5\">\n" +
                        "                                            <div class=\"bg-gradient-primary shadow-primary border-radius-lg py-1\">\n" +
                        "                                                <h6 class=\"text-white text-capitalize ps-3\">Child " + i + "</h6>\n" +
                        "                                            </div>\n" +
                        "                                        </div>\n" +
                        "                                        <div class=\"child" + i + "\" mb-5>\n" +
                        "                                            <div class=\"row mt-5\">\n" +
                        "                                                <h6>Main Details</h6>\n" +
                        "                                                <div class=\"col-12 col-sm-4\">\n" +
                        "                                                    <input type=\"hidden\" id=\"child" + i + "Id\" name=\"child" + i + "Id\"><div class=\"input-group input-group-dynamic mb-3 is-filled is-filled\">\n" +
                        "                                                        <label class=\"form-label\">First Name *</label>\n" +
                        "                                                        <input type=\"text\" name=\"child" + i + "FName\"\n" +
                        "                                                               class=\"multisteps-form__input form-control\">\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                        <label class=\"form-label\">Middle Name</label>\n" +
                        "                                                        <input type=\"text\" name=\"child" + i + "MName\"\n" +
                        "                                                               class=\"multisteps-form__input form-control\">\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                        <label class=\"form-label\">Last Name *</label>\n" +
                        "                                                        <input type=\"text\" name=\"child" + i + "LName\"\n" +
                        "                                                               class=\"multisteps-form__input form-control\">\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                        <label class=\"form-label\">DOB *</label>\n" +
                        "                                                        <input type=\"date\" name=\"child" + i + "Dob\"\n" +
                        "                                                               class=\"multisteps-form__input form-control\">\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "<div class=\"input-group input-group-static mb-3\">\n" +
                        "                                                        <label for=\"exampleFormControlSelect1\" class=\"ms-0\">Gender</label>\n" +
                        "                                                        <select class=\"form-control form-dark-select\" name=\"child" + i + "Gender\">\n" +
                        "                                                            <option value=\"Male\">Male</option>\n" +
                        "                                                            <option value=\"Female\">Female</option>\n" +
                        "                                                        </select>\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-static mb-3\">\n" +
                        "                                                        <label for=\"exampleFormControlSelect1\" class=\"ms-0\">Member Group</label>\n" +
                        "                                                        <select class=\"form-control form-dark-select\" name=\"child" + i + "MemberGroup\">\n" +
                        "                                                            " + membrGroupsOptions + " \n" +
                        "                                                        </select>\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                </div>\n" +
                        "\n" +
                        "                                                <div class=\"col-12 col-sm-4\">\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                        <label class=\"form-label\">Phone</label>\n" +
                        "                                                        <input type=\"tel\" name=\"child" + i + "Phone\"\n" +
                        "                                                               class=\"multisteps-form__input form-control\">\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                        <label class=\"form-label\">Email</label>\n" +
                        "                                                        <input type=\"email\" name=\"child" + i + "Email\"\n" +
                        "                                                               class=\"multisteps-form__input form-control\">\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                        <label class=\"form-label\">Address</label>\n" +
                        "                                                        <input type=\"text\" name=\"child" + i + "Address\"\n" +
                        "                                                               class=\"multisteps-form__input form-control\">\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                        <label class=\"form-label\">Residence</label>\n" +
                        "                                                        <input type=\"text\" name=\"child" + i + "Residence\"\n" +
                        "                                                               class=\"multisteps-form__input form-control\">\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-static mb-3\">\n" +
                        "                                                        <label for=\"exampleFormControlSelect1\" class=\"ms-0\">Zone</label>\n" +
                        "                                                        <select class=\"form-control form-dark-select\" name=\"child" + i + "Zone\"> " + zonesOptions + "\n" +
                        "                                                        </select>\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-static mb-3\">\n" +
                        "                                                        <label for=\"exampleFormControlSelect1\" class=\"ms-0\">Preferred Service</label>\n" +
                        "                                                        <select class=\"form-control form-dark-select\" name=\"child" + i + "PreferredService\"> " + servicesOptions + "\n" +
                        "                                                        </select>\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                </div>\n" +
                        "\n" +
                        "                                                <div class=\"col-12 col-sm-4\">\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                        <label class=\"form-label\">Profession</label>\n" +
                        "                                                        <input type=\"text\" name=\"child" + i + "Profession\"\n" +
                        "                                                               class=\"multisteps-form__input form-control\">\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                        <label class=\"form-label\">Occupation</label>\n" +
                        "                                                        <input type=\"text\" name=\"child" + i + "Occupation\"\n" +
                        "                                                               class=\"multisteps-form__input form-control\">\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                        <label class=\"form-label\">Employer</label>\n" +
                        "                                                        <input type=\"text\" name=\"child" + i + "Employer\"\n" +
                        "                                                               class=\"multisteps-form__input form-control\">\n" +
                        "                                                    </div>\n" +
                        "                                                   <div class=\"avatar avatar-xxl position-relative\">\n" +
                        "                                                    <img src=\"../assets/img/member.png\" id=\"child" + i + "Image\" class=\"border-radius-md\"\n" +
                        "                                                         alt=\"team-2\">\n" +
                        "                                                    <a href=\"javascript:;\" id=\"editChild" + i + "ImageButton\"\n" +
                        "                                                       class=\"btn btn-sm btn-icon-only bg-gradient-primary position-absolute bottom-0 end-0 mb-n2 me-n2\">\n" +
                        "                                                    <span class=\"material-icons text-xs top-0 mt-n2\" data-bs-toggle=\"tooltip\" data-bs-placement=\"top\" title\n" +
                        "                                                          aria-hidden=\"true\" data-bs-original-title=\"Edit Image\" aria-label=\"Edit Image\">\n" +
                        "                                                        edit\n" +
                        "                                                    </span>\n" +
                        "                                                    </a>\n" +
                        "                                                </div>\n" +
                        "                                                <input type=\"hidden\" name=\"imageUrl\" id=\"child" + i + "ImageUrl\">\n" +
                        "                                                <input type=\"file\" id=\"child" + i + "ImageInput\" accept=\"image/*\" style=\"display: none;\">" +
                        "                                                </div>\n" +

                        "                                               <script>\n" +
                        "                                                    $(document).ready(function () {\n" +
                        "                                                        const editButton = $('#editChild" + i + "ImageButton');\n" +
                        "                                                        const imageInput = $('#child" + i + "ImageInput');\n" +
                        "                                                        const image = $('#child" + i + "Image');\n" +
                        "\n" +
                        "                                                        editButton.click(function () {\n" +
                        "                                                            imageInput.click();\n" +
                        "                                                        });\n" +
                        "\n" +
                        "                                                        imageInput.change(function() {\n" +
                        "                                                            const selectedFile = imageInput[0].files[0];\n" +
                        "\n" +
                        "                                                            if (selectedFile) {\n" +
                        "                                                                const formData = new FormData();\n" +
                        "                                                                formData.append('pictureFile', selectedFile);\n" +
                        "                                                                formData.append('morepath', \"MemberImages\");\n" +
                        "\n" +
                        "                                                                $.ajax({\n" +
                        "                                                                    url: '/upload',\n" +
                        "                                                                    method: 'POST',\n" +
                        "                                                                    data: formData,\n" +
                        "                                                                    contentType: false,\n" +
                        "                                                                    processData: false,\n" +
                        "                                                                    success: function(response) {\n" +
                        "                                                                        console.log(response)\n" +
                        "                                                                        if (response.querystatus === \"Image uploaded successfully\") {\n" +
                        "                                                                            image.attr('src', response.path);" +
                        "                                                                           $('#child" + i + "ImageUrl').val(response.path);\n" +
                        "                                                                        } else {\n" +
                        "                                                                            alert('Upload failed: ' + response.error);\n" +
                        "                                                                        }\n" +
                        "                                                                    },\n" +
                        "                                                                    error: function() {\n" +
                        "                                                                        alert('An error occurred while uploading the image.');\n" +
                        "                                                                    }\n" +
                        "                                                                });\n" +
                        "                                                            }\n" +
                        "                                                        });\n" +
                        "                                                    });\n" +
                        "                                                </script>" +
                        "                                            </div>\n" +
                        "                                            <div class=\"row mt-5\">\n" +
                        "                                                <h6>Baptism Details</h6>\n" +
                        "                                                <div class=\"col-12 col-sm-12\">\n" +
                        "                                                    <div class=\"form-check form-check-info text-start my-3\">\n" +
                        "                                                        <input class=\"form-check-input\" type=\"checkbox\"\n" +
                        "                                                               id=\"child" + i + "Baptised\" name=\"child" + i + "Baptised\">\n" +
                        "                                                        <label class=\"form-check-label\">\n" +
                        "                                                            Baptized\n" +
                        "                                                        </label>\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div id=\"child" + i + "BaptismDiv\" class=\"hidden mt-3\">\n" +
                        "                                                        <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                            <label class=\"form-label\">Date of baptism *</label>\n" +
                        "                                                            <input type=\"date\" name=\"child" + i + "BaptismDate\"\n" +
                        "                                                                   class=\"multisteps-form__input form-control\">\n" +
                        "                                                        </div>\n" +
                        "\n" +
                        "                                                        <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                            <label class=\"form-label\">Place of baptism *</label>\n" +
                        "                                                            <input type=\"text\" name=\"child" + i + "BaptismPlace\"\n" +
                        "                                                                   class=\"multisteps-form__input form-control\">\n" +
                        "                                                        </div>\n" +
                        "\n" +
                        "                                                        <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                            <label class=\"form-label\">Baptised By *</label>\n" +
                        "                                                            <input type=\"text\" name=\"child" + i + "BaptisedBy\"\n" +
                        "                                                                   class=\"multisteps-form__input form-control\">\n" +
                        "                                                        </div>\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <script>\n" +
                        "                                                        $(\"#child" + i + "Baptised\").change(function () {\n" +
                        "                                                            if (this.checked) {\n" +
                        "                                                                $('#child" + i + "BaptismDiv').removeClass('hidden');" +
                        "updateFormAttr('#child" + i + "BaptismDiv', true);\n" +
                        "                                                            } else {\n" +
                        "                                                                $('#child" + i + "BaptismDiv').addClass('hidden');" +
                        "updateFormAttr('#child" + i + "BaptismDiv', false);\n" +
                        "                                                            }\n" +
                        "                                                        });\n" +
                        "                                                    </script>\n" +
                        "\n" +
                        "                                                </div>\n" +
                        "                                            </div>\n" +
                        "                                            <div class=\"row mt-5\">\n" +
                        "                                                <h6>Confirmation Details</h6>\n" +
                        "                                                <div class=\"col-12 col-sm-12\">\n" +
                        "                                                    <div class=\"form-check form-check-info text-start my-3\">\n" +
                        "                                                        <input class=\"form-check-input\" type=\"checkbox\"\n" +
                        "                                                               id=\"child" + i + "Confirmed\" name=\"child" + i + "Confirmed\">\n" +
                        "                                                        <label class=\"form-check-label\">\n" +
                        "                                                            Confirmed\n" +
                        "                                                        </label>\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div id=\"child" + i + "ConfirmationDiv\" class=\"hidden mt-3\">\n" +
                        "                                                        <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                            <label class=\"form-label\">Date of confirmation *</label>\n" +
                        "                                                            <input type=\"date\" name=\"child" + i + "ConfirmationDate\"\n" +
                        "                                                                   class=\"multisteps-form__input form-control\">\n" +
                        "                                                        </div>\n" +
                        "\n" +
                        "                                                        <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                            <label class=\"form-label\">Place of confirmation *</label>\n" +
                        "                                                            <input type=\"text\" name=\"child" + i + "ConfirmationPlace\"\n" +
                        "                                                                   class=\"multisteps-form__input form-control\">\n" +
                        "                                                        </div>\n" +
                        "\n" +
                        "                                                        <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                            <label class=\"form-label\">Confirmed By *</label>\n" +
                        "                                                            <input type=\"text\" name=\"child" + i + "ConfirmedBy\"\n" +
                        "                                                                   class=\"multisteps-form__input form-control\">\n" +
                        "                                                        </div>\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <script>\n" +
                        "                                                        $(\"#child" + i + "Confirmed\").change(function () {\n" +
                        "                                                            if (this.checked) {\n" +
                        "                                                                $('#child" + i + "ConfirmationDiv').removeClass('hidden');" +
                        "updateFormAttr('#child" + i + "ConfirmationDiv', true);\n" +
                        "                                                            } else {\n" +
                        "                                                                $('#child" + i + "ConfirmationDiv').addClass('hidden');" +
                        "updateFormAttr('#child" + i + "ConfirmationDiv', false);\n" +
                        "                                                            }\n" +
                        "                                                        });\n" +
                        "                                                    </script>\n" +
                        "\n" +
                        "                                                </div>\n" +
                        "                                            </div>\n" +
                        "                                        </div>";

                    $('#childDetailsedit').append(childOption);

                    $(`#editModal input[name="child${i}Id"]`).val(child.id);
                    $(`#editModal input[name="child${i}FName"]`).val(child.fName).parent().addClass('is-filled');
                    $(`#editModal input[name="child${i}MName"]`).val(child.mName).parent().addClass('is-filled');
                    $(`#editModal input[name="child${i}LName"]`).val(child.lName).parent().addClass('is-filled');
                    $(`#editModal input[name="child${i}Email"]`).val(child.email).parent().addClass('is-filled');
                    $(`#editModal input[name="child${i}Dob"]`).val(child.dob).parent().addClass('is-filled');
                    $(`#editModal input[name="child${i}Address"]`).val(child.address).parent().addClass('is-filled');
                    $(`#editModal input[name="child${i}Residence"]`).val(child.residence).parent().addClass('is-filled');
                    $(`#editModal input[name="child${i}Profession"]`).val(child.profession).parent().addClass('is-filled');
                    $(`#editModal input[name="child${i}Occupation"]`).val(child.occupation).parent().addClass('is-filled');
                    $(`#editModal input[name="child${i}Employer"]`).val(child.employer).parent().addClass('is-filled');
                    $(`#editModal input[name="child${i}Zone"]`).val(child.zone.id);
                    $(`#editModal input[name="child${i}MemberGroup"]`).val(child.memberGroup.id);
                    $(`#editModal input[name="child${i}MaritalStatus"]`).val(child.maritalStatus);
                    $(`#editModal input[name="child${i}Gender"]`).val(child.gender);
                    $(`#editModal input[name="child${i}PreferredService"]`).val(child.preferredService.id);
                    $(`#editModal input[name="child${i}Phone"]`).val(child.phone).parent().addClass('is-filled');
                    if (child.image !== "") {
                        $(`#editModal input[name="child${i}Image"]`).attr('src', child.image);
                    }

                    if (child.baptismStatus === 'true') {
                        $(`#editModal #child${i}Baptised`).prop('checked', true);
                        $(`#editModal #child${i}BaptismDiv`).removeClass('hidden');
                        $(`#editModal input[name="child${i}BaptismDate"]`).val(child.baptismDetails.baptismDate).parent().addClass('is-filled');
                        $(`#editModal input[name="child${i}BaptismPlace"]`).val(child.baptismDetails.baptismPlace).parent().addClass('is-filled');
                        $(`#editModal input[name="child${i}BaptisedBy"]`).val(child.baptismDetails.baptisedBy).parent().addClass('is-filled');
                    }

                    if (child.confirmationStatus === 'true') {
                        $(`#editModal #child${i}Confirmed`).prop('checked', true);
                        $(`#editModal #child${i}ConfirmationDiv`).removeClass('hidden');
                        $(`#editModal input[name="child${i}ConfirmationDate"]`).val(child.confirmationDetails.confirmationDate).parent().addClass('is-filled');
                        $(`#editModal input[name="child${i}ConfirmationPlace"]`).val(child.confirmationDetails.confirmationPlace).parent().addClass('is-filled');
                        $(`#editModal input[name="child${i}ConfirmedBy"]`).val(child.confirmationDetails.confirmedBy).parent().addClass('is-filled');
                    }
                })
            } else if (response.childrenAsMother.length > 0) {
                $.each(response.childrenAsMother, async function (position, child) {

                    let i = position + 1;

                    $('#childDetailsedit').removeClass('hidden');
                    var membrGroupsOptions = await populateMemberGroups(child.memberGroup.id);
                    var zonesOptions = await populateZones(child.zone.id);
                    var servicesOptions = await populateChurchServices(child.preferredService.id);
                    let childOption = "<div id=\"toggleChild" + i + "Details\"\n" +
                        "                                             class=\"card-header p-0 position-relative mt-n4 mx-3 z-index-2 mt-5\">\n" +
                        "                                            <div class=\"bg-gradient-primary shadow-primary border-radius-lg py-1\">\n" +
                        "                                                <h6 class=\"text-white text-capitalize ps-3\">Child " + i + "</h6>\n" +
                        "                                            </div>\n" +
                        "                                        </div>\n" +
                        "                                        <div class=\"child" + i + "\" mb-5>\n" +
                        "                                            <div class=\"row mt-5\">\n" +
                        "                                                <h6>Main Details</h6>\n" +
                        "                                                <div class=\"col-12 col-sm-4\">\n" +
                        "                                                    <input type=\"hidden\" id=\"child" + i + "Id\" name=\"child" + i + "Id\"><div class=\"input-group input-group-dynamic mb-3 is-filled is-filled\">\n" +
                        "                                                        <label class=\"form-label\">First Name *</label>\n" +
                        "                                                        <input type=\"text\" name=\"child" + i + "FName\"\n" +
                        "                                                               class=\"multisteps-form__input form-control\">\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                        <label class=\"form-label\">Middle Name</label>\n" +
                        "                                                        <input type=\"text\" name=\"child" + i + "MName\"\n" +
                        "                                                               class=\"multisteps-form__input form-control\">\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                        <label class=\"form-label\">Last Name *</label>\n" +
                        "                                                        <input type=\"text\" name=\"child" + i + "LName\"\n" +
                        "                                                               class=\"multisteps-form__input form-control\">\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                        <label class=\"form-label\">DOB *</label>\n" +
                        "                                                        <input type=\"date\" name=\"child" + i + "Dob\"\n" +
                        "                                                               class=\"multisteps-form__input form-control\">\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "<div class=\"input-group input-group-static mb-3\">\n" +
                        "                                                        <label for=\"exampleFormControlSelect1\" class=\"ms-0\">Gender</label>\n" +
                        "                                                        <select class=\"form-control form-dark-select\" name=\"child" + i + "Gender\">\n" +
                        "                                                            <option value=\"Male\">Male</option>\n" +
                        "                                                            <option value=\"Female\">Female</option>\n" +
                        "                                                        </select>\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-static mb-3\">\n" +
                        "                                                        <label for=\"exampleFormControlSelect1\" class=\"ms-0\">Member Group</label>\n" +
                        "                                                        <select class=\"form-control form-dark-select\" name=\"child" + i + "MemberGroup\">\n" +
                        "                                                            " + membrGroupsOptions + " \n" +
                        "                                                        </select>\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                </div>\n" +
                        "\n" +
                        "                                                <div class=\"col-12 col-sm-4\">\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                        <label class=\"form-label\">Phone</label>\n" +
                        "                                                        <input type=\"tel\" name=\"child" + i + "Phone\"\n" +
                        "                                                               class=\"multisteps-form__input form-control\">\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                        <label class=\"form-label\">Email</label>\n" +
                        "                                                        <input type=\"email\" name=\"child" + i + "Email\"\n" +
                        "                                                               class=\"multisteps-form__input form-control\">\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                        <label class=\"form-label\">Address</label>\n" +
                        "                                                        <input type=\"text\" name=\"child" + i + "Address\"\n" +
                        "                                                               class=\"multisteps-form__input form-control\">\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                        <label class=\"form-label\">Residence</label>\n" +
                        "                                                        <input type=\"text\" name=\"child" + i + "Residence\"\n" +
                        "                                                               class=\"multisteps-form__input form-control\">\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-static mb-3\">\n" +
                        "                                                        <label for=\"exampleFormControlSelect1\" class=\"ms-0\">Zone</label>\n" +
                        "                                                        <select class=\"form-control form-dark-select\" name=\"child" + i + "Zone\"> " + zonesOptions + "\n" +
                        "                                                        </select>\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-static mb-3\">\n" +
                        "                                                        <label for=\"exampleFormControlSelect1\" class=\"ms-0\">Preferred Service</label>\n" +
                        "                                                        <select class=\"form-control form-dark-select\" name=\"child" + i + "PreferredService\"> " + servicesOptions + "\n" +
                        "                                                        </select>\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                </div>\n" +
                        "\n" +
                        "                                                <div class=\"col-12 col-sm-4\">\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                        <label class=\"form-label\">Profession</label>\n" +
                        "                                                        <input type=\"text\" name=\"child" + i + "Profession\"\n" +
                        "                                                               class=\"multisteps-form__input form-control\">\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                        <label class=\"form-label\">Occupation</label>\n" +
                        "                                                        <input type=\"text\" name=\"child" + i + "Occupation\"\n" +
                        "                                                               class=\"multisteps-form__input form-control\">\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                        <label class=\"form-label\">Employer</label>\n" +
                        "                                                        <input type=\"text\" name=\"child" + i + "Employer\"\n" +
                        "                                                               class=\"multisteps-form__input form-control\">\n" +
                        "                                                    </div>\n" +
                        "                                                   <div class=\"avatar avatar-xxl position-relative\">\n" +
                        "                                                    <img src=\"../assets/img/member.png\" id=\"child" + i + "Image\" class=\"border-radius-md\"\n" +
                        "                                                         alt=\"team-2\">\n" +
                        "                                                    <a href=\"javascript:;\" id=\"editChild" + i + "ImageButton\"\n" +
                        "                                                       class=\"btn btn-sm btn-icon-only bg-gradient-primary position-absolute bottom-0 end-0 mb-n2 me-n2\">\n" +
                        "                                                    <span class=\"material-icons text-xs top-0 mt-n2\" data-bs-toggle=\"tooltip\" data-bs-placement=\"top\" title\n" +
                        "                                                          aria-hidden=\"true\" data-bs-original-title=\"Edit Image\" aria-label=\"Edit Image\">\n" +
                        "                                                        edit\n" +
                        "                                                    </span>\n" +
                        "                                                    </a>\n" +
                        "                                                </div>\n" +
                        "                                                <input type=\"hidden\" name=\"imageUrl\" id=\"child" + i + "ImageUrl\">\n" +
                        "                                                <input type=\"file\" id=\"child" + i + "ImageInput\" accept=\"image/*\" style=\"display: none;\">" +
                        "                                                </div>\n" +

                        "                                               <script>\n" +
                        "                                                    $(document).ready(function () {\n" +
                        "                                                        const editButton = $('#editChild" + i + "ImageButton');\n" +
                        "                                                        const imageInput = $('#child" + i + "ImageInput');\n" +
                        "                                                        const image = $('#child" + i + "Image');\n" +
                        "\n" +
                        "                                                        editButton.click(function () {\n" +
                        "                                                            imageInput.click();\n" +
                        "                                                        });\n" +
                        "\n" +
                        "                                                        imageInput.change(function() {\n" +
                        "                                                            const selectedFile = imageInput[0].files[0];\n" +
                        "\n" +
                        "                                                            if (selectedFile) {\n" +
                        "                                                                const formData = new FormData();\n" +
                        "                                                                formData.append('pictureFile', selectedFile);\n" +
                        "                                                                formData.append('morepath', \"MemberImages\");\n" +
                        "\n" +
                        "                                                                $.ajax({\n" +
                        "                                                                    url: '/upload',\n" +
                        "                                                                    method: 'POST',\n" +
                        "                                                                    data: formData,\n" +
                        "                                                                    contentType: false,\n" +
                        "                                                                    processData: false,\n" +
                        "                                                                    success: function(response) {\n" +
                        "                                                                        console.log(response)\n" +
                        "                                                                        if (response.querystatus === \"Image uploaded successfully\") {\n" +
                        "                                                                            image.attr('src', response.path);" +
                        "                                                                           $('#child" + i + "ImageUrl').val(response.path);\n" +
                        "                                                                        } else {\n" +
                        "                                                                            alert('Upload failed: ' + response.error);\n" +
                        "                                                                        }\n" +
                        "                                                                    },\n" +
                        "                                                                    error: function() {\n" +
                        "                                                                        alert('An error occurred while uploading the image.');\n" +
                        "                                                                    }\n" +
                        "                                                                });\n" +
                        "                                                            }\n" +
                        "                                                        });\n" +
                        "                                                    });\n" +
                        "                                                </script>" +
                        "                                            </div>\n" +
                        "                                            <div class=\"row mt-5\">\n" +
                        "                                                <h6>Baptism Details</h6>\n" +
                        "                                                <div class=\"col-12 col-sm-12\">\n" +
                        "                                                    <div class=\"form-check form-check-info text-start my-3\">\n" +
                        "                                                        <input class=\"form-check-input\" type=\"checkbox\"\n" +
                        "                                                               id=\"child" + i + "Baptised\" name=\"child" + i + "Baptised\">\n" +
                        "                                                        <label class=\"form-check-label\">\n" +
                        "                                                            Baptized\n" +
                        "                                                        </label>\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div id=\"child" + i + "BaptismDiv\" class=\"hidden mt-3\">\n" +
                        "                                                        <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                            <label class=\"form-label\">Date of baptism *</label>\n" +
                        "                                                            <input type=\"date\" name=\"child" + i + "BaptismDate\"\n" +
                        "                                                                   class=\"multisteps-form__input form-control\">\n" +
                        "                                                        </div>\n" +
                        "\n" +
                        "                                                        <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                            <label class=\"form-label\">Place of baptism *</label>\n" +
                        "                                                            <input type=\"text\" name=\"child" + i + "BaptismPlace\"\n" +
                        "                                                                   class=\"multisteps-form__input form-control\">\n" +
                        "                                                        </div>\n" +
                        "\n" +
                        "                                                        <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                            <label class=\"form-label\">Baptised By *</label>\n" +
                        "                                                            <input type=\"text\" name=\"child" + i + "BaptisedBy\"\n" +
                        "                                                                   class=\"multisteps-form__input form-control\">\n" +
                        "                                                        </div>\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <script>\n" +
                        "                                                        $(\"#child" + i + "Baptised\").change(function () {\n" +
                        "                                                            if (this.checked) {\n" +
                        "                                                                $('#child" + i + "BaptismDiv').removeClass('hidden');" +
                        "updateFormAttr('#child" + i + "BaptismDiv', true);\n" +
                        "                                                            } else {\n" +
                        "                                                                $('#child" + i + "BaptismDiv').addClass('hidden');" +
                        "updateFormAttr('#child" + i + "BaptismDiv', false);\n" +
                        "                                                            }\n" +
                        "                                                        });\n" +
                        "                                                    </script>\n" +
                        "\n" +
                        "                                                </div>\n" +
                        "                                            </div>\n" +
                        "                                            <div class=\"row mt-5\">\n" +
                        "                                                <h6>Confirmation Details</h6>\n" +
                        "                                                <div class=\"col-12 col-sm-12\">\n" +
                        "                                                    <div class=\"form-check form-check-info text-start my-3\">\n" +
                        "                                                        <input class=\"form-check-input\" type=\"checkbox\"\n" +
                        "                                                               id=\"child" + i + "Confirmed\" name=\"child" + i + "Confirmed\">\n" +
                        "                                                        <label class=\"form-check-label\">\n" +
                        "                                                            Confirmed\n" +
                        "                                                        </label>\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <div id=\"child" + i + "ConfirmationDiv\" class=\"hidden mt-3\">\n" +
                        "                                                        <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                            <label class=\"form-label\">Date of confirmation *</label>\n" +
                        "                                                            <input type=\"date\" name=\"child" + i + "ConfirmationDate\"\n" +
                        "                                                                   class=\"multisteps-form__input form-control\">\n" +
                        "                                                        </div>\n" +
                        "\n" +
                        "                                                        <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                            <label class=\"form-label\">Place of confirmation *</label>\n" +
                        "                                                            <input type=\"text\" name=\"child" + i + "ConfirmationPlace\"\n" +
                        "                                                                   class=\"multisteps-form__input form-control\">\n" +
                        "                                                        </div>\n" +
                        "\n" +
                        "                                                        <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                        "                                                            <label class=\"form-label\">Confirmed By *</label>\n" +
                        "                                                            <input type=\"text\" name=\"child" + i + "ConfirmedBy\"\n" +
                        "                                                                   class=\"multisteps-form__input form-control\">\n" +
                        "                                                        </div>\n" +
                        "                                                    </div>\n" +
                        "\n" +
                        "                                                    <script>\n" +
                        "                                                        $(\"#child" + i + "Confirmed\").change(function () {\n" +
                        "                                                            if (this.checked) {\n" +
                        "                                                                $('#child" + i + "ConfirmationDiv').removeClass('hidden');" +
                        "updateFormAttr('#child" + i + "ConfirmationDiv', true);\n" +
                        "                                                            } else {\n" +
                        "                                                                $('#child" + i + "ConfirmationDiv').addClass('hidden');" +
                        "updateFormAttr('#child" + i + "ConfirmationDiv', false);\n" +
                        "                                                            }\n" +
                        "                                                        });\n" +
                        "                                                    </script>\n" +
                        "\n" +
                        "                                                </div>\n" +
                        "                                            </div>\n" +
                        "                                        </div>";

                    $('#childDetailsedit').append(childOption);

                    $(`#editModal input[name="child${i}Id"]`).val(child.id);
                    $(`#editModal input[name="child${i}FName"]`).val(child.fName).parent().addClass('is-filled');
                    $(`#editModal input[name="child${i}MName"]`).val(child.mName).parent().addClass('is-filled');
                    $(`#editModal input[name="child${i}LName"]`).val(child.lName).parent().addClass('is-filled');
                    $(`#editModal input[name="child${i}Email"]`).val(child.email).parent().addClass('is-filled');
                    $(`#editModal input[name="child${i}Dob"]`).val(child.dob).parent().addClass('is-filled');
                    $(`#editModal input[name="child${i}Address"]`).val(child.address).parent().addClass('is-filled');
                    $(`#editModal input[name="child${i}Residence"]`).val(child.residence).parent().addClass('is-filled');
                    $(`#editModal input[name="child${i}Profession"]`).val(child.profession).parent().addClass('is-filled');
                    $(`#editModal input[name="child${i}Occupation"]`).val(child.occupation).parent().addClass('is-filled');
                    $(`#editModal input[name="child${i}Employer"]`).val(child.employer).parent().addClass('is-filled');
                    $(`#editModal input[name="child${i}Zone"]`).val(child.zone.id);
                    $(`#editModal input[name="child${i}MemberGroup"]`).val(child.memberGroup.id);
                    $(`#editModal input[name="child${i}MaritalStatus"]`).val(child.maritalStatus);
                    $(`#editModal input[name="child${i}Gender"]`).val(child.gender);
                    $(`#editModal input[name="child${i}PreferredService"]`).val(child.preferredService.id);
                    $(`#editModal input[name="child${i}Phone"]`).val(child.phone).parent().addClass('is-filled');
                    if (child.image !== "") {
                        $(`#editModal input[name="child${i}Image"]`).attr('src', child.image);
                    }

                    if (child.baptismStatus === 'true') {
                        $(`#editModal #child${i}Baptised`).prop('checked', true);
                        $(`#editModal #child${i}BaptismDiv`).removeClass('hidden');
                        $(`#editModal input[name="child${i}BaptismDate"]`).val(child.baptismDetails.baptismDate).parent().addClass('is-filled');
                        $(`#editModal input[name="child${i}BaptismPlace"]`).val(child.baptismDetails.baptismPlace).parent().addClass('is-filled');
                        $(`#editModal input[name="child${i}BaptisedBy"]`).val(child.baptismDetails.baptisedBy).parent().addClass('is-filled');
                    }

                    if (child.confirmationStatus === 'true') {
                        $(`#editModal #child${i}Confirmed`).prop('checked', true);
                        $(`#editModal #child${i}ConfirmationDiv`).removeClass('hidden');
                        $(`#editModal input[name="child${i}ConfirmationDate"]`).val(child.confirmationDetails.confirmationDate).parent().addClass('is-filled');
                        $(`#editModal input[name="child${i}ConfirmationPlace"]`).val(child.confirmationDetails.confirmationPlace).parent().addClass('is-filled');
                        $(`#editModal input[name="child${i}ConfirmedBy"]`).val(child.confirmationDetails.confirmedBy).parent().addClass('is-filled');
                    }
                })
            }


        }).fail(function (response) {
            console.log(response);
        })
    } else if (model === 'committeeMembers') {
        $.ajax({
            url: '/api/member/committee/members/get/' + id,
            contentType: 'application/json'
        }).done(function (response) {
            $('#editModal input[name="id"]').val(response.id);
            $('#editModal input[name="position"]').val(response.position).parent().addClass('is-filled');
            if (response.member != null) {
                $('#editModal input[name="member"]').val(response.member.id);
                $('#editModal .searchPerson').val(response.member.name).parent().addClass('is-filled');
            }
        }).fail(function (response) {
            console.log(response);
        })
    }
    $('#editModal').modal('show');
}

function searchPersonA() {
    var query = $('#searchMember').val();
    console.log(query);
    recTypeUrl = '/api/member/search';
    $.post(recTypeUrl, {query: query}, function (results) {
        console.log(results);
        var dropdown = $("#member .searchResults");
        if (query === "") {
            dropdown.empty();
            return;
        }
        dropdown.empty();
        console.log(results.length);
        for (var i = 0; i < results.length; i++) {
            var member = results[i];
            console.log(member);
            var option = $("<option>")
                .attr("value", member.id)
                .text(member.member)
                .click(function () {
                    $('#searchMember').val($(this).text());
                    $('#memberId').val($(this).val());
                    dropdown.empty()
                })
                .hover(function () {
                    $(this).addClass('highlighted');
                }, function () {
                    $(this).removeClass('highlighted');
                });
            dropdown.append(option);
        }
    });
}


function updateAmount(row) {
    // Get the quantity and rate values from the inputs
    var quantity = parseFloat(row.find(".quantity-input").val());
    var rate = parseFloat(row.find(".rate-input").val());

    // Calculate the amount and update the amount input
    var amount = quantity * rate;
    row.find(".amount-input").val(amount.toFixed(2));

    // Return the calculated amount for summing up
    return amount;
}

function updateTotalAmountEdit() {
    var totalAmount = 0;
    // Loop through each row and calculate the amount
    $("#editVouchersTable tbody tr").each(function () {
        totalAmount += updateAmount($(this));
    });

    // Update the total amount display element
    localStorage.setItem("totalBeforeTax", totalAmount.toFixed(2));
    $("#editBeforeTax").text("Before tax: " + totalAmount.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,'));

    let withholding = parseFloat($('#editModal input[name="withholding"]').val());
    let vat = parseFloat($('#editModal input[name="vat"]').val());
    let profFees = parseFloat($('#editModal input[name="profFees"]').val());
    let payableAmount = totalAmount - (((withholding / 100) * totalAmount) + ((vat / 100) * totalAmount) + ((profFees / 100) * totalAmount));
    $('#editTotalPayable').text("Total Payable: " + payableAmount.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,'));
    localStorage.setItem("totalPayable", payableAmount);
}

function openChurchModal(model) {
    $.ajax({
        url: '/settings/farm',
        contentType: 'application/json'
    }).done(function (response) {
        $('#churchDetailsModal input[name="id"]').val(response.id);
        $('#churchDetailsModal input[name="name"]').val(response.name).parent().addClass('is-filled');
        $('#churchDetailsModal input[name="region"]').val(response.region).parent().addClass('is-filled');
        $('#churchDetailsModal input[name="email"]').val(response.email).parent().addClass('is-filled');
        $('#churchDetailsModal input[name="address"]').val(response.address).parent().addClass('is-filled');
        $('#churchDetailsModal input[name="country"]').val(response.country).parent().addClass('is-filled');
        $('#churchDetailsModal input[name="phone"]').val(response.phone).parent().addClass('is-filled');
        $('#churchDetailsModal input[name="zip"]').val(response.zip).parent().addClass('is-filled');
    }).fail(function (response) {
        console.log(response);
    })
    $('#churchDetailsModal').modal('show');
}

function openEditChurchModal(id) {
    $.ajax({
        url: '/hidden/hidden/stuff/farm/get/' + id,
        contentType: 'application/json'
    }).done(function (response) {
        $('#churchDetailsModal input[name="id"]').val(response.id);
        $('#churchDetailsModal input[name="name"]').val(response.name).parent().addClass('is-filled');
        $('#churchDetailsModal input[name="region"]').val(response.region).parent().addClass('is-filled');
        $('#churchDetailsModal input[name="email"]').val(response.email).parent().addClass('is-filled');
        $('#churchDetailsModal input[name="address"]').val(response.address).parent().addClass('is-filled');
        $('#churchDetailsModal input[name="country"]').val(response.country).parent().addClass('is-filled');
        $('#churchDetailsModal input[name="phone"]').val(response.phone).parent().addClass('is-filled');
        $('#churchDetailsModal input[name="zip"]').val(response.zip).parent().addClass('is-filled');
    }).fail(function (response) {
        console.log(response);
    })
    $('#churchDetailsModal').modal('show');
}

function openOfficialsModal(model) {
    $.ajax({
        url: '/settings/official',
        contentType: 'application/json'
    }).done(function (response) {
        console.log(response);
        $('#churchOfficialsModal input[name="id"]').val(response.id);
        $('#churchOfficialsModal select[name="accountant"]').val(response.accountant.id).parent().addClass('is-filled');
        $('#churchOfficialsModal select[name="seniorPastor"]').val(response.seniorPastor.id).parent().addClass('is-filled');
        $('#churchOfficialsModal select[name="secondSignatory"]').val(response.secondSignatory.id).parent().addClass('is-filled');
        $('#churchOfficialsModal select[name="treasurer"]').val(response.treasurer.id).parent().addClass('is-filled');

    }).fail(function (response) {
        console.log(response);
    })
    $('#churchOfficialsModal').modal('show');
}

function submitSignupForm(event) {
    event.preventDefault();

    let username = $('#signupForm input[name="username"]').val();
    let email = $(' #signupForm input[name="email"]').val();
    let password = "password";

    let requestBody = {
        username: username,
        email: email,
        password: password
    }

    $.ajax({
        url: '/api/auth/signup',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(requestBody),
        success: function (response) {
            console.log(response);
            if (response.message === "User registered successfully!") {
                $('#infoModal .modal-body').empty();
                $('#infoModal .modal-body').append("<p>" + response.message + "</p>");
                $('#infoModal').modal('show');
                $('#newMemberModal').modal('hide');

                $('#infoModal #confirmOk').click(function () {
                    window.location.reload();
                })
            }
        },
        error: function (xhr, status, error) {
            const responseJSON = xhr.responseJSON;
            if (responseJSON && responseJSON.message) {
                $('#infoModal .modal-body').empty();
                $('#infoModal .modal-body').append("<p>" + responseJSON.message + "</p>");
                $('#infoModal').modal('show');
                $('#newMemberModal').modal('hide');
            } else {
                $('#infoModal .modal-body').empty();
                $('#infoModal .modal-body').append("<p>An error occurred. Please try again later.</p>");
                $('#infoModal').modal('show');
                $('#newMemberModal').modal('hide');
            }
        }
    });
}

$(document).on('click', '.delete-modal', function (event) {
    event.preventDefault();
    var button = $(this);
    var url = button.data('link');
    var deleteModal = $('#myModaldelete');
    $(deleteModal).find('#confirmdeletebutt').on('click', function () {
        window.location = url;
    })
})


function updateFormAttr(section, action) {
    console.log(section);
    if (action === true) {
        $(section + ' input, textarea, select').each(function () {
            if ($(this).attr('data-required') === 'required') {
                console.log(section + ' required');
                $(this).attr('required', 'required');
            }
        });
    } else {
        console.log(section + ' not required');
        $(section + ' input[required],textarea[required],select[required]').attr('data-required', 'required').removeAttr('required');
    }
}


$(document).ready(function () {

    $("#numberOfChildren").on('input', async function () {
        var selectedValue = $(this).val();
        const childDetailsContainer = $('#childDetails');

        if (selectedValue > 0) {
            childDetailsContainer.removeClass('hidden');
            updateFormAttr('#childDetails', true);

            var membrGroupsOptions = await populateMemberGroups(null);
            var zonesOptions = await populateZones(null);
            var servicesOptions = await populateChurchServices(null);
            for (let i = 1; i <= selectedValue; i++) {
                let childOption = "<div id=\"toggleChild" + i + "Details\"\n" +
                    "                                             class=\"card-header p-0 position-relative mt-n4 mx-3 z-index-2 mt-5\">\n" +
                    "                                            <div class=\"bg-gradient-primary shadow-primary border-radius-lg py-1\">\n" +
                    "                                                <h6 class=\"text-white text-capitalize ps-3\">Child " + i + "</h6>\n" +
                    "                                            </div>\n" +
                    "                                        </div>\n" +
                    "                                        <div class=\"child" + i + "\" mb-5>\n" +
                    "                                            <div class=\"row mt-5\">\n" +
                    "                                                <h6>Main Details</h6>\n" +
                    "                                                <div class=\"col-12 col-sm-4\">\n" +
                    "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled is-filled\">\n" +
                    "                                                        <label class=\"form-label\">First Name *</label>\n" +
                    "                                                        <input type=\"text\" name=\"child" + i + "FName\"\n" +
                    "                                                               class=\"multisteps-form__input form-control\">\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                        <label class=\"form-label\">Middle Name</label>\n" +
                    "                                                        <input type=\"text\" name=\"child" + i + "MName\"\n" +
                    "                                                               class=\"multisteps-form__input form-control\">\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                        <label class=\"form-label\">Last Name *</label>\n" +
                    "                                                        <input type=\"text\" name=\"child" + i + "LName\"\n" +
                    "                                                               class=\"multisteps-form__input form-control\">\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                        <label class=\"form-label\">DOB *</label>\n" +
                    "                                                        <input type=\"date\" name=\"child" + i + "Dob\"\n" +
                    "                                                               class=\"multisteps-form__input form-control\">\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "<div class=\"input-group input-group-static mb-3\">\n" +
                    "                                                        <label for=\"exampleFormControlSelect1\" class=\"ms-0\">Gender</label>\n" +
                    "                                                        <select class=\"form-control form-dark-select\" name=\"child" + i + "Gender\">\n" +
                    "                                                            <option value=\"Male\">Male</option>\n" +
                    "                                                            <option value=\"Female\">Female</option>\n" +
                    "                                                        </select>\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-static mb-3\">\n" +
                    "                                                        <label for=\"exampleFormControlSelect1\" class=\"ms-0\">Member Group</label>\n" +
                    "                                                        <select class=\"form-control form-dark-select\" name=\"child" + i + "MemberGroup\">\n" +
                    "                                                            " + membrGroupsOptions + " \n" +
                    "                                                        </select>\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                </div>\n" +
                    "\n" +
                    "                                                <div class=\"col-12 col-sm-4\">\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                        <label class=\"form-label\">Phone</label>\n" +
                    "                                                        <input type=\"tel\" name=\"child" + i + "Phone\"\n" +
                    "                                                               class=\"multisteps-form__input form-control\">\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                        <label class=\"form-label\">Email</label>\n" +
                    "                                                        <input type=\"email\" name=\"child" + i + "Email\"\n" +
                    "                                                               class=\"multisteps-form__input form-control\">\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                        <label class=\"form-label\">Address</label>\n" +
                    "                                                        <input type=\"text\" name=\"child" + i + "Address\"\n" +
                    "                                                               class=\"multisteps-form__input form-control\">\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                        <label class=\"form-label\">Residence</label>\n" +
                    "                                                        <input type=\"text\" name=\"child" + i + "Residence\"\n" +
                    "                                                               class=\"multisteps-form__input form-control\">\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-static mb-3\">\n" +
                    "                                                        <label for=\"exampleFormControlSelect1\" class=\"ms-0\">Zone</label>\n" +
                    "                                                        <select class=\"form-control form-dark-select\" name=\"child" + i + "Zone\"> " + zonesOptions + "\n" +
                    "                                                        </select>\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-static mb-3\">\n" +
                    "                                                        <label for=\"exampleFormControlSelect1\" class=\"ms-0\">Preferred Service</label>\n" +
                    "                                                        <select class=\"form-control form-dark-select\" name=\"child" + i + "PreferredService\"> " + servicesOptions + "\n" +
                    "                                                        </select>\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                </div>\n" +
                    "\n" +
                    "                                                <div class=\"col-12 col-sm-4\">\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                        <label class=\"form-label\">Profession</label>\n" +
                    "                                                        <input type=\"text\" name=\"child" + i + "Profession\"\n" +
                    "                                                               class=\"multisteps-form__input form-control\">\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                        <label class=\"form-label\">Occupation</label>\n" +
                    "                                                        <input type=\"text\" name=\"child" + i + "Occupation\"\n" +
                    "                                                               class=\"multisteps-form__input form-control\">\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                        <label class=\"form-label\">Employer</label>\n" +
                    "                                                        <input type=\"text\" name=\"child" + i + "Employer\"\n" +
                    "                                                               class=\"multisteps-form__input form-control\">\n" +
                    "                                                    </div>\n" +
                    "                                                   <div class=\"avatar avatar-xxl position-relative\">\n" +
                    "                                                    <img src=\"../assets/img/member.png\" id=\"editChild" + i + "Image\" class=\"border-radius-md\"\n" +
                    "                                                         alt=\"team-2\">\n" +
                    "                                                    <a href=\"javascript:;\" id=\"editChild" + i + "ImageButton\"\n" +
                    "                                                       class=\"btn btn-sm btn-icon-only bg-gradient-primary position-absolute bottom-0 end-0 mb-n2 me-n2\">\n" +
                    "                                                    <span class=\"material-icons text-xs top-0 mt-n2\" data-bs-toggle=\"tooltip\" data-bs-placement=\"top\" title\n" +
                    "                                                          aria-hidden=\"true\" data-bs-original-title=\"Edit Image\" aria-label=\"Edit Image\">\n" +
                    "                                                        edit\n" +
                    "                                                    </span>\n" +
                    "                                                    </a>\n" +
                    "                                                </div>\n" +
                    "                                                <input type=\"hidden\" name=\"imageUrl\" id=\"editChild" + i + "ImageUrl\">\n" +
                    "                                                <input type=\"file\" id=\"editChild" + i + "ImageInput\" accept=\"image/*\" style=\"display: none;\">" +
                    "                                                </div>\n" +

                    "                                               <script>\n" +
                    "                                                    $(document).ready(function () {\n" +
                    "                                                        const editButton = $('#editChild" + i + "ImageButton');\n" +
                    "                                                        const imageInput = $('#editChild" + i + "ImageInput');\n" +
                    "                                                        const image = $('#editChild" + i + "Image');\n" +
                    "\n" +
                    "                                                        editButton.click(function () {\n" +
                    "                                                            imageInput.click();\n" +
                    "                                                        });\n" +
                    "\n" +
                    "                                                        imageInput.change(function() {\n" +
                    "                                                            const selectedFile = imageInput[0].files[0];\n" +
                    "\n" +
                    "                                                            if (selectedFile) {\n" +
                    "                                                                const formData = new FormData();\n" +
                    "                                                                formData.append('pictureFile', selectedFile);\n" +
                    "                                                                formData.append('morepath', \"MemberImages\");\n" +
                    "\n" +
                    "                                                                $.ajax({\n" +
                    "                                                                    url: '/upload',\n" +
                    "                                                                    method: 'POST',\n" +
                    "                                                                    data: formData,\n" +
                    "                                                                    contentType: false,\n" +
                    "                                                                    processData: false,\n" +
                    "                                                                    success: function(response) {\n" +
                    "                                                                        console.log(response)\n" +
                    "                                                                        if (response.querystatus === \"Image uploaded successfully\") {\n" +
                    "                                                                            image.attr('src', response.path);" +
                    "                                                                           $('#editChild" + i + "ImageUrl').val(response.path);\n" +
                    "                                                                        } else {\n" +
                    "                                                                            alert('Upload failed: ' + response.error);\n" +
                    "                                                                        }\n" +
                    "                                                                    },\n" +
                    "                                                                    error: function() {\n" +
                    "                                                                        alert('An error occurred while uploading the image.');\n" +
                    "                                                                    }\n" +
                    "                                                                });\n" +
                    "                                                            }\n" +
                    "                                                        });\n" +
                    "                                                    });\n" +
                    "                                                </script>" +
                    "                                            </div>\n" +
                    "                                            <div class=\"row mt-5\">\n" +
                    "                                                <h6>Baptism Details</h6>\n" +
                    "                                                <div class=\"col-12 col-sm-12\">\n" +
                    "                                                    <div class=\"form-check form-check-info text-start my-3\">\n" +
                    "                                                        <input class=\"form-check-input\" type=\"checkbox\"\n" +
                    "                                                               id=\"child" + i + "Baptised\" name=\"child" + i + "Baptised\">\n" +
                    "                                                        <label class=\"form-check-label\">\n" +
                    "                                                            Baptized\n" +
                    "                                                        </label>\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div id=\"child" + i + "BaptismDiv\" class=\"hidden mt-3\">\n" +
                    "                                                        <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                            <label class=\"form-label\">Date of baptism *</label>\n" +
                    "                                                            <input type=\"date\" name=\"child" + i + "BaptismDate\"\n" +
                    "                                                                   class=\"multisteps-form__input form-control\">\n" +
                    "                                                        </div>\n" +
                    "\n" +
                    "                                                        <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                            <label class=\"form-label\">Place of baptism *</label>\n" +
                    "                                                            <input type=\"text\" name=\"child" + i + "BaptismPlace\"\n" +
                    "                                                                   class=\"multisteps-form__input form-control\">\n" +
                    "                                                        </div>\n" +
                    "\n" +
                    "                                                        <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                            <label class=\"form-label\">Baptised By *</label>\n" +
                    "                                                            <input type=\"text\" name=\"child" + i + "BaptisedBy\"\n" +
                    "                                                                   class=\"multisteps-form__input form-control\">\n" +
                    "                                                        </div>\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <script>\n" +
                    "                                                        $(\"#child" + i + "Baptised\").change(function () {\n" +
                    "                                                            if (this.checked) {\n" +
                    "                                                                $('#child" + i + "BaptismDiv').removeClass('hidden');" +
                    "updateFormAttr('#child" + i + "BaptismDiv', true);\n" +
                    "                                                            } else {\n" +
                    "                                                                $('#child" + i + "BaptismDiv').addClass('hidden');" +
                    "updateFormAttr('#child" + i + "BaptismDiv', false);\n" +
                    "                                                            }\n" +
                    "                                                        });\n" +
                    "                                                    </script>\n" +
                    "\n" +
                    "                                                </div>\n" +
                    "                                            </div>\n" +
                    "                                            <div class=\"row mt-5\">\n" +
                    "                                                <h6>Confirmation Details</h6>\n" +
                    "                                                <div class=\"col-12 col-sm-12\">\n" +
                    "                                                    <div class=\"form-check form-check-info text-start my-3\">\n" +
                    "                                                        <input class=\"form-check-input\" type=\"checkbox\"\n" +
                    "                                                               id=\"child" + i + "Confirmed\" name=\"child" + i + "Confirmed\">\n" +
                    "                                                        <label class=\"form-check-label\">\n" +
                    "                                                            Confirmed\n" +
                    "                                                        </label>\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div id=\"child" + i + "ConfirmationDiv\" class=\"hidden mt-3\">\n" +
                    "                                                        <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                            <label class=\"form-label\">Date of confirmation *</label>\n" +
                    "                                                            <input type=\"date\" name=\"child" + i + "ConfirmationDate\"\n" +
                    "                                                                   class=\"multisteps-form__input form-control\">\n" +
                    "                                                        </div>\n" +
                    "\n" +
                    "                                                        <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                            <label class=\"form-label\">Place of confirmation *</label>\n" +
                    "                                                            <input type=\"text\" name=\"child" + i + "ConfirmationPlace\"\n" +
                    "                                                                   class=\"multisteps-form__input form-control\">\n" +
                    "                                                        </div>\n" +
                    "\n" +
                    "                                                        <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                            <label class=\"form-label\">Confirmed By *</label>\n" +
                    "                                                            <input type=\"text\" name=\"child" + i + "ConfirmedBy\"\n" +
                    "                                                                   class=\"multisteps-form__input form-control\">\n" +
                    "                                                        </div>\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <script>\n" +
                    "                                                        $(\"#child" + i + "Confirmed\").change(function () {\n" +
                    "                                                            if (this.checked) {\n" +
                    "                                                                $('#child" + i + "ConfirmationDiv').removeClass('hidden');" +
                    "updateFormAttr('#child" + i + "ConfirmationDiv', true);\n" +
                    "                                                            } else {\n" +
                    "                                                                $('#child" + i + "ConfirmationDiv').addClass('hidden');" +
                    "updateFormAttr('#child" + i + "ConfirmationDiv', false);\n" +
                    "                                                            }\n" +
                    "                                                        });\n" +
                    "                                                    </script>\n" +
                    "\n" +
                    "                                                </div>\n" +
                    "                                            </div>\n" +
                    "                                        </div>";

                childDetailsContainer.append(childOption);
            }

        } else {
            childDetailsContainer.addClass('hidden');
            updateFormAttr('#childDetails', false);
            childDetailsContainer.empty();
        }
    });

    $("#numberOfChildrenedit").on('input', async function () {
        var selectedValue = $(this).val();

        if (selectedValue > 0) {
            $('#childDetailsedit').removeClass('hidden');
            updateFormAttr('#childDetailsedit', true);
            var membrGroupsOptions = await populateMemberGroups(null);
            var zonesOptions = await populateZones(null);
            var servicesOptions = await populateChurchServices(null);
            for (let i = 1; i <= selectedValue; i++) {
                let childOption = "<div id=\"toggleChild" + i + "Details\"\n" +
                    "                                             class=\"card-header p-0 position-relative mt-n4 mx-3 z-index-2 mt-5\">\n" +
                    "                                            <div class=\"bg-gradient-primary shadow-primary border-radius-lg py-1\">\n" +
                    "                                                <h6 class=\"text-white text-capitalize ps-3\">Child " + i + "</h6>\n" +
                    "                                            </div>\n" +
                    "                                        </div>\n" +
                    "                                        <div class=\"child" + i + "\" mb-5>\n" +
                    "                                            <div class=\"row mt-5\">\n" +
                    "                                                <h6>Main Details</h6>\n" +
                    "                                                <div class=\"col-12 col-sm-4\">\n" +
                    "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled is-filled\">\n" +
                    "                                                        <label class=\"form-label\">First Name *</label>\n" +
                    "                                                        <input type=\"text\" name=\"child" + i + "FName\"\n" +
                    "                                                               class=\"multisteps-form__input form-control\">\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                        <label class=\"form-label\">Middle Name</label>\n" +
                    "                                                        <input type=\"text\" name=\"child" + i + "MName\"\n" +
                    "                                                               class=\"multisteps-form__input form-control\">\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                        <label class=\"form-label\">Last Name *</label>\n" +
                    "                                                        <input type=\"text\" name=\"child" + i + "LName\"\n" +
                    "                                                               class=\"multisteps-form__input form-control\">\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                        <label class=\"form-label\">DOB *</label>\n" +
                    "                                                        <input type=\"date\" name=\"child" + i + "Dob\"\n" +
                    "                                                               class=\"multisteps-form__input form-control\">\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "<div class=\"input-group input-group-static mb-3\">\n" +
                    "                                                        <label for=\"exampleFormControlSelect1\" class=\"ms-0\">Gender</label>\n" +
                    "                                                        <select class=\"form-control form-dark-select\" name=\"child" + i + "Gender\">\n" +
                    "                                                            <option value=\"Male\">Male</option>\n" +
                    "                                                            <option value=\"Female\">Female</option>\n" +
                    "                                                        </select>\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-static mb-3\">\n" +
                    "                                                        <label for=\"exampleFormControlSelect1\" class=\"ms-0\">Member Group</label>\n" +
                    "                                                        <select class=\"form-control form-dark-select\" name=\"child" + i + "MemberGroup\">\n" +
                    "                                                            " + membrGroupsOptions + " \n" +
                    "                                                        </select>\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                </div>\n" +
                    "\n" +
                    "                                                <div class=\"col-12 col-sm-4\">\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                        <label class=\"form-label\">Phone</label>\n" +
                    "                                                        <input type=\"tel\" name=\"child" + i + "Phone\"\n" +
                    "                                                               class=\"multisteps-form__input form-control\">\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                        <label class=\"form-label\">Email</label>\n" +
                    "                                                        <input type=\"email\" name=\"child" + i + "Email\"\n" +
                    "                                                               class=\"multisteps-form__input form-control\">\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                        <label class=\"form-label\">Address</label>\n" +
                    "                                                        <input type=\"text\" name=\"child" + i + "Address\"\n" +
                    "                                                               class=\"multisteps-form__input form-control\">\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                        <label class=\"form-label\">Residence</label>\n" +
                    "                                                        <input type=\"text\" name=\"child" + i + "Residence\"\n" +
                    "                                                               class=\"multisteps-form__input form-control\">\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-static mb-3\">\n" +
                    "                                                        <label for=\"exampleFormControlSelect1\" class=\"ms-0\">Zone</label>\n" +
                    "                                                        <select class=\"form-control form-dark-select\" name=\"child" + i + "Zone\"> " + zonesOptions + "\n" +
                    "                                                        </select>\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-static mb-3\">\n" +
                    "                                                        <label for=\"exampleFormControlSelect1\" class=\"ms-0\">Preferred Service</label>\n" +
                    "                                                        <select class=\"form-control form-dark-select\" name=\"child" + i + "PreferredService\"> " + servicesOptions + "\n" +
                    "                                                        </select>\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                </div>\n" +
                    "\n" +
                    "                                                <div class=\"col-12 col-sm-4\">\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                        <label class=\"form-label\">Profession</label>\n" +
                    "                                                        <input type=\"text\" name=\"child" + i + "Profession\"\n" +
                    "                                                               class=\"multisteps-form__input form-control\">\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                        <label class=\"form-label\">Occupation</label>\n" +
                    "                                                        <input type=\"text\" name=\"child" + i + "Occupation\"\n" +
                    "                                                               class=\"multisteps-form__input form-control\">\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                        <label class=\"form-label\">Employer</label>\n" +
                    "                                                        <input type=\"text\" name=\"child" + i + "Employer\"\n" +
                    "                                                               class=\"multisteps-form__input form-control\">\n" +
                    "                                                    </div>\n" +
                    "                                                   <div class=\"avatar avatar-xxl position-relative\">\n" +
                    "                                                    <img src=\"../assets/img/member.png\" id=\"child" + i + "Image\" class=\"border-radius-md\"\n" +
                    "                                                         alt=\"team-2\">\n" +
                    "                                                    <a href=\"javascript:;\" id=\"editChild" + i + "ImageButton\"\n" +
                    "                                                       class=\"btn btn-sm btn-icon-only bg-gradient-primary position-absolute bottom-0 end-0 mb-n2 me-n2\">\n" +
                    "                                                    <span class=\"material-icons text-xs top-0 mt-n2\" data-bs-toggle=\"tooltip\" data-bs-placement=\"top\" title\n" +
                    "                                                          aria-hidden=\"true\" data-bs-original-title=\"Edit Image\" aria-label=\"Edit Image\">\n" +
                    "                                                        edit\n" +
                    "                                                    </span>\n" +
                    "                                                    </a>\n" +
                    "                                                </div>\n" +
                    "                                                <input type=\"hidden\" name=\"imageUrl\" id=\"child" + i + "ImageUrl\">\n" +
                    "                                                <input type=\"file\" id=\"child" + i + "ImageInput\" accept=\"image/*\" style=\"display: none;\">" +
                    "                                                </div>\n" +

                    "                                               <script>\n" +
                    "                                                    $(document).ready(function () {\n" +
                    "                                                        const editButton = $('#editChild" + i + "ImageButton');\n" +
                    "                                                        const imageInput = $('#child" + i + "ImageInput');\n" +
                    "                                                        const image = $('#child" + i + "Image');\n" +
                    "\n" +
                    "                                                        editButton.click(function () {\n" +
                    "                                                            imageInput.click();\n" +
                    "                                                        });\n" +
                    "\n" +
                    "                                                        imageInput.change(function() {\n" +
                    "                                                            const selectedFile = imageInput[0].files[0];\n" +
                    "\n" +
                    "                                                            if (selectedFile) {\n" +
                    "                                                                const formData = new FormData();\n" +
                    "                                                                formData.append('pictureFile', selectedFile);\n" +
                    "                                                                formData.append('morepath', \"MemberImages\");\n" +
                    "\n" +
                    "                                                                $.ajax({\n" +
                    "                                                                    url: '/upload',\n" +
                    "                                                                    method: 'POST',\n" +
                    "                                                                    data: formData,\n" +
                    "                                                                    contentType: false,\n" +
                    "                                                                    processData: false,\n" +
                    "                                                                    success: function(response) {\n" +
                    "                                                                        console.log(response)\n" +
                    "                                                                        if (response.querystatus === \"Image uploaded successfully\") {\n" +
                    "                                                                            image.attr('src', response.path);" +
                    "                                                                           $('#child" + i + "ImageUrl').val(response.path);\n" +
                    "                                                                        } else {\n" +
                    "                                                                            alert('Upload failed: ' + response.error);\n" +
                    "                                                                        }\n" +
                    "                                                                    },\n" +
                    "                                                                    error: function() {\n" +
                    "                                                                        alert('An error occurred while uploading the image.');\n" +
                    "                                                                    }\n" +
                    "                                                                });\n" +
                    "                                                            }\n" +
                    "                                                        });\n" +
                    "                                                    });\n" +
                    "                                                </script>" +
                    "                                            </div>\n" +
                    "                                            <div class=\"row mt-5\">\n" +
                    "                                                <h6>Baptism Details</h6>\n" +
                    "                                                <div class=\"col-12 col-sm-12\">\n" +
                    "                                                    <div class=\"form-check form-check-info text-start my-3\">\n" +
                    "                                                        <input class=\"form-check-input\" type=\"checkbox\"\n" +
                    "                                                               id=\"child" + i + "Baptised\" name=\"child" + i + "Baptised\">\n" +
                    "                                                        <label class=\"form-check-label\">\n" +
                    "                                                            Baptized\n" +
                    "                                                        </label>\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div id=\"child" + i + "BaptismDiv\" class=\"hidden mt-3\">\n" +
                    "                                                        <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                            <label class=\"form-label\">Date of baptism *</label>\n" +
                    "                                                            <input type=\"date\" name=\"child" + i + "BaptismDate\"\n" +
                    "                                                                   class=\"multisteps-form__input form-control\">\n" +
                    "                                                        </div>\n" +
                    "\n" +
                    "                                                        <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                            <label class=\"form-label\">Place of baptism *</label>\n" +
                    "                                                            <input type=\"text\" name=\"child" + i + "BaptismPlace\"\n" +
                    "                                                                   class=\"multisteps-form__input form-control\">\n" +
                    "                                                        </div>\n" +
                    "\n" +
                    "                                                        <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                            <label class=\"form-label\">Baptised By *</label>\n" +
                    "                                                            <input type=\"text\" name=\"child" + i + "BaptisedBy\"\n" +
                    "                                                                   class=\"multisteps-form__input form-control\">\n" +
                    "                                                        </div>\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <script>\n" +
                    "                                                        $(\"#child" + i + "Baptised\").change(function () {\n" +
                    "                                                            if (this.checked) {\n" +
                    "                                                                $('#child" + i + "BaptismDiv').removeClass('hidden');" +
                    "updateFormAttr('#child" + i + "BaptismDiv', true);\n" +
                    "                                                            } else {\n" +
                    "                                                                $('#child" + i + "BaptismDiv').addClass('hidden');" +
                    "updateFormAttr('#child" + i + "BaptismDiv', false);\n" +
                    "                                                            }\n" +
                    "                                                        });\n" +
                    "                                                    </script>\n" +
                    "\n" +
                    "                                                </div>\n" +
                    "                                            </div>\n" +
                    "                                            <div class=\"row mt-5\">\n" +
                    "                                                <h6>Confirmation Details</h6>\n" +
                    "                                                <div class=\"col-12 col-sm-12\">\n" +
                    "                                                    <div class=\"form-check form-check-info text-start my-3\">\n" +
                    "                                                        <input class=\"form-check-input\" type=\"checkbox\"\n" +
                    "                                                               id=\"child" + i + "Confirmed\" name=\"child" + i + "Confirmed\">\n" +
                    "                                                        <label class=\"form-check-label\">\n" +
                    "                                                            Confirmed\n" +
                    "                                                        </label>\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <div id=\"child" + i + "ConfirmationDiv\" class=\"hidden mt-3\">\n" +
                    "                                                        <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                            <label class=\"form-label\">Date of confirmation *</label>\n" +
                    "                                                            <input type=\"date\" name=\"child" + i + "ConfirmationDate\"\n" +
                    "                                                                   class=\"multisteps-form__input form-control\">\n" +
                    "                                                        </div>\n" +
                    "\n" +
                    "                                                        <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                            <label class=\"form-label\">Place of confirmation *</label>\n" +
                    "                                                            <input type=\"text\" name=\"child" + i + "ConfirmationPlace\"\n" +
                    "                                                                   class=\"multisteps-form__input form-control\">\n" +
                    "                                                        </div>\n" +
                    "\n" +
                    "                                                        <div class=\"input-group input-group-dynamic mb-3 is-filled\">\n" +
                    "                                                            <label class=\"form-label\">Confirmed By *</label>\n" +
                    "                                                            <input type=\"text\" name=\"child" + i + "ConfirmedBy\"\n" +
                    "                                                                   class=\"multisteps-form__input form-control\">\n" +
                    "                                                        </div>\n" +
                    "                                                    </div>\n" +
                    "\n" +
                    "                                                    <script>\n" +
                    "                                                        $(\"#child" + i + "Confirmed\").change(function () {\n" +
                    "                                                            if (this.checked) {\n" +
                    "                                                                $('#child" + i + "ConfirmationDiv').removeClass('hidden');" +
                    "updateFormAttr('#child" + i + "ConfirmationDiv', true);\n" +
                    "                                                            } else {\n" +
                    "                                                                $('#child" + i + "ConfirmationDiv').addClass('hidden');" +
                    "updateFormAttr('#child" + i + "ConfirmationDiv', false);\n" +
                    "                                                            }\n" +
                    "                                                        });\n" +
                    "                                                    </script>\n" +
                    "\n" +
                    "                                                </div>\n" +
                    "                                            </div>\n" +
                    "                                        </div>";

                $('#childDetailsedit').append(childOption);
            }

        } else {
            $('#childDetailsedit').addClass('hidden');
            updateFormAttr('#childDetailsedit', false);
            $('#childDetailsedit').empty();
        }
    });

    var selectedValue = $('#numberOfChildren').val();
    for (let i = 1; i <= selectedValue; i++) {
        $("#toggleChild" + i + "Details").click(function () {
            $(".child" + i + "").toggleClass('hidden');
        });
    }

    $('#openingBalanceModal select[name="bank"]').on("change", function () {
        var selectedValue = $(this).val();
        $.ajax({
            url: '/finance/bank/get/' + selectedValue,
            contentType: 'application/json'
        }).done(function (accountData) {
        }).done(function (accountData) {
            $('#openingBalanceModal input[name="amount"]').val(accountData.openingBalance).parent().addClass('is-filled');
        }).fail(function (response) {
            console.log(response);
        })
    });

    $('input[type="date"]').val(Date.now()).parent().addClass('is-filled');

    $('#membersTable').DataTable({
        responsive: true
    });

    $('#sample').DataTable({
        responsive: true
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
            }).done(function (object) {
                console.log(object);

                var html = createTableRow(Myurl, JSON.parse(object));

                var dataTable = $(maintable).DataTable();
                dataTable.clear();
                const tr = $(html);
                dataTable.row.add(tr[0]).draw();
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


    /*$(document).ready(function() {
        $('table#sample').DataTable({
            ajax: {
                contentType: 'application/json',
                url: '/api/member/get/all',
                type: 'POST',
                data: function (d) {
                    console.log(d)
                    return JSON.stringify(d);
                }
            },
            serverSide: true,
            processing: true,
            columns: [
                {
                    data: null,
                    render: function (data, type, row) {
                        if (data.image) {
                            return '<img src="' + data.image + '" alt="dp" class="avatar avatar-sm me-3">';
                        } else {
                            return '<img src="/assets/img/member.png" alt="Dp" class="avatar avatar-sm me-3">';
                        }
                    }
                },
                {
                    data: null,
                    render: function (data, type, row) {
                        return data.fName + ' ' + data.mName + ' ' + data.lName;
                    }
                },
                { data: 'zone.zone' },
                { data: 'maritalStatus' },
                { data: 'memberGroup.group' },
                { data: 'phone' },
                {
                    data: null,
                    render: function (data, type, row) {
                        var actionsHtml = '<a class="btn btn-success" href="/api/member/view/' + data.id + '"><i class="fa fa-eye" aria-hidden="true"></i></a>';
                        actionsHtml += '|<a class="btn btn-warning" onclick="openEditModal(\'members\', \'' + data.id + '\')"><i class="fa fa-pencil" aria-hidden="true"></i></a>';
                        actionsHtml += '|<a class="delete-modal btn btn-danger" data-bs-toggle="modal" data-bs-target="#myModaldelete" data-link="/api/member/delete/' + data.id + '"><i class="fa fa-trash" aria-hidden="true"></i></a>';
                        return actionsHtml;
                    }
                }
            ]
        });
    })*/


    setTimeout(function () {
        var successMessage = document.getElementById('successMessage');
        if (successMessage) {
            successMessage.style.display = 'none';
        }
    }, 5000);

    setTimeout(function () {
        var errorMessage = document.getElementById('errorMessage');
        if (errorMessage) {
            errorMessage.style.display = 'none';
        }
    }, 5000);

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

    $.ajax({
        contentType: "application/json",
        url: "/finance/banking/get/all",
        dataType: 'json',
        success: function (data) {
            $.each(data, function (index, option) {
                $('select[name="bank"]').append("<option value='" + option.id + "'>" + option.accountName + "</option>");
            });
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
})

function registerMember(event) {
    event.preventDefault();

    let spouseId = $('#newMemberModal .spouseId').val();
    let image = $('#newMemberModal input[name="imageUrl"]').val();
    let first = $('#newMemberModal input[name="fName"]').val();
    let second = $('#newMemberModal input[name="mName"]').val();
    let last = $('#newMemberModal input[name="lName"]').val();
    let dob = $('#newMemberModal input[name="dob"]').val();

    let gender = $('#newMemberModal select[name="gender"]').val();
    let membershipDuration = $('#newMemberModal select[name="membershipDuration"]').val();
    let memberGroup = $('#newMemberModal select[name="memberGroup"]').val();
    let enrolmentDate = $('#newMemberModal input[name="enrolmentDate"]').val();
    let enrolmentPlace = $('#newMemberModal input[name="enrolmentPlace"]').val();
    let phone = $('#newMemberModal input[name="phone"]').val();
    let email = $('#newMemberModal input[name="email"]').val();

    let address = $('#newMemberModal input[name="address"]').val();
    let residence = $('#newMemberModal input[name="residence"]').val();
    let zone = $('#newMemberModal select[name="zone"]').val();
    let preferredService = $('#newMemberModal select[name="preferredService"]').val();

    let profession = $('#newMemberModal input[name="profession"]').val();
    let occupation = $('#newMemberModal input[name="occupation"]').val();
    let employer = $('#newMemberModal input[name="employer"]').val();

    let talents = $('#newMemberModal #talents').val();

    let baptismDate = $('#newMemberModal input[name="baptismDate"]').val();
    let placeOfBaptism = $('#newMemberModal input[name="baptismPlace"]').val();
    let baptisedBy = $('#newMemberModal input[name="baptisedBy"]').val();

    let confirmationDate = $('#newMemberModal input[name="confirmationDate"]').val();
    let placeOfConfirmation = $('#newMemberModal input[name="confirmationPlace"]').val();
    let confirmedBy = $('#newMemberModal input[name="confirmedBy"]').val();

    let mainDetails = {
        spouseId, spouseId,
        image: image,
        first: first,
        second: second,
        last: last,
        dob: dob,
        gender: gender,
        membershipDuration, membershipDuration,
        isMember: isMember,
        memberGroup: memberGroup,
        enrolmentPlace,
        enrolmentDate,
        phone: phone,
        email: email,
        address: address,
        residence: residence,
        zone: zone,
        talents: talents,
        preferredService: preferredService,
        profession: profession,
        occupation: occupation,
        employer: employer,
        baptised: baptised,
        baptismDate: baptismDate,
        placeOfBaptism: placeOfBaptism,
        baptisedBy: baptisedBy,
        confirmed: confirmed,
        confirmationDate: confirmationDate,
        placeOfConfirmation: placeOfConfirmation,
        confirmedBy: confirmedBy
    };

    let maritalStatus = $('#newMemberModal select[name="maritalStatus"]').val();
    let marriageType = $('#newMemberModal select[name="marriageType"]').val();
    let weddingDate = $('#newMemberModal input[name="weddingDate"]').val();
    let weddingPlace = $('#newMemberModal input[name="weddingPlace"]').val();
    let spouseImage = $('#newMemberModal #spouseImageUrl').val();
    let spouseFName;
    let spouseSecond;
    let spouseLast;

    if (maritalStatus === 'Married') {
        spouseFName = $('#newMemberModal #spouseDetails input[name="spouseFName"]').val();
        spouseSecond = $('#newMemberModal #spouseDetails input[name="spouseMName"]').val();
        spouseLast = $('#newMemberModal #spouseDetails input[name="spouseLName"]').val();
    } else if (maritalStatus === 'Widow' || maritalStatus === 'Widower') {
        spouseFName = $('#newMemberModal #deceasedSpouseDetails input[name="spouseFName"]').val();
        spouseSecond = $('#newMemberModal #deceasedSpouseDetails input[name="spouseMName"]').val();
        spouseLast = $('#newMemberModal #deceasedSpouseDetails input[name="spouseLName"]').val();
    }

    let spouseDob = $('#newMemberModal input[name="spouseDob"]').val();

    let spouseGender = $('#newMemberModal select[name="spouseGender"]').val();
    let spouseMemberGroup = $('#newMemberModal select[name="spouseMemberGroup"]').val();
    let spousePhone = $('#newMemberModal input[name="spousePhone"]').val();
    let spouseEmail = $('#newMemberModal input[name="spouseEmail"]').val();

    let spouseAddress = $('#newMemberModal input[name="spouseAddress"]').val();
    let spouseResidence = $('#newMemberModal input[name="spouseResidence"]').val();
    let spouseZone = $('#newMemberModal select[name="spouseZone"]').val();
    let spousePreferredService = $('#newMemberModal select[name="spousePreferredService"]').val();

    let spouseProfession = $('#newMemberModal input[name="spouseProfession"]').val();
    let spouseOccupation = $('#newMemberModal input[name="spouseOccupation"]').val();
    let spouseEmployer = $('#newMemberModal input[name="spouseEmployer"]').val();

    let spouseBaptismDate = $('#newMemberModal input[name="spouseBaptismDate"]').val();
    let spousePlaceOfBaptism = $('#newMemberModal input[name="spouseBaptismPlace"]').val();
    let spouseBaptisedBy = $('#newMemberModal input[name="spouseBaptisedBy"]').val();

    let spouseConfirmationDate = $('#newMemberModal input[name="spouseConfirmationDate"]').val();
    let spousePlaceOfConfirmation = $('#newMemberModal input[name="spouseConfirmationPlace"]').val();
    let spouseConfirmedBy = $('#newMemberModal input[name="spouseConfirmedBy"]').val();

    let spouseDetails = {
        maritalStatus: maritalStatus,
        marriageType: marriageType,
        weddingDate: weddingDate,
        weddingPlace: weddingPlace,
        spouseImage: spouseImage,
        spouseFName: spouseFName,
        spouseSecond: spouseSecond,
        spouseLast: spouseLast,
        spouseDob: spouseDob,
        spouseGender: spouseGender,
        spouseMemberGroup: spouseMemberGroup,
        spousePhone: spousePhone,
        spouseEmail: spouseEmail,
        spouseAddress: spouseAddress,
        spouseResidence: spouseResidence,
        spouseZone: spouseZone,
        spousePreferredService: spousePreferredService,
        spouseProfession: spouseProfession,
        spouseOccupation: spouseOccupation,
        spouseEmployer: spouseEmployer,
        spouseBaptised: spouseBaptised,
        spouseBaptismDate: spouseBaptismDate,
        spousePlaceOfBaptism: spousePlaceOfBaptism,
        spouseBaptisedBy: spouseBaptisedBy,
        spouseConfirmed: spouseConfirmed,
        spouseConfirmationDate: spouseConfirmationDate,
        spousePlaceOfConfirmation: spousePlaceOfConfirmation,
        spouseConfirmedBy: spouseConfirmedBy
    };

    let numberOfChildren = $('#numberOfChildren').val();

    let children = [];

    for (let i = 1; i <= numberOfChildren; i++) {
        let childImage = $('#editChild' + i + 'ImageUrl').val();
        let childFName = $(`input[name="child${i}FName"]`).val();
        let childMName = $(`input[name="child${i}MName"]`).val();
        let childLName = $(`input[name="child${i}LName"]`).val();
        let childDob = $(`input[name="child${i}Dob"]`).val();
        let childGender = $(`select[name="child${i}Gender"]`).val();
        let childMemberGroup = $(`select[name="child${i}MemberGroup"]`).val();
        let childPhone = $(`input[name="child${i}Phone"]`).val();
        let childEmail = $(`input[name="child${i}Email"]`).val();
        let childAddress = $(`input[name="child${i}Address"]`).val();
        let childResidence = $(`input[name="child${i}Residence"]`).val();
        let childZone = $(`select[name="child${i}Zone"]`).val();
        let childPreferredService = $(`select[name="child${i}PreferredService"]`).val();
        let childProfession = $(`input[name="child${i}Profession"]`).val();
        let childOccupation = $(`input[name="child${i}Occupation"]`).val();
        let childEmployer = $(`input[name="child${i}Employer"]`).val();
        let childBaptised = $(`input[name="child${i}Baptised"]`).is(':checked');
        let childBaptismDate = $(`input[name="child${i}BaptismDate"]`).val();
        let childPlaceOfBaptism = $(`input[name="child${i}BaptismPlace"]`).val();
        let childBaptisedBy = $(`input[name="child${i}BaptisedBy"]`).val();
        let childConfirmed = $(`input[name="child${i}Confirmed"]`).is(':checked');
        let childConfirmationDate = $(`input[name="child${i}ConfirmationDate"]`).val();
        let childPlaceOfConfirmation = $(`input[name="child${i}ConfirmationPlace"]`).val();
        let childConfirmedBy = $(`input[name="child${i}ConfirmedBy"]`).val();

        let childObject = {
            childImage,
            childFName,
            childMName,
            childLName,
            childDob,
            childGender,
            childMemberGroup,
            childPhone,
            childEmail,
            childAddress,
            childResidence,
            childZone,
            childPreferredService,
            childProfession,
            childOccupation,
            childEmployer,
            childBaptised,
            childBaptismDate,
            childPlaceOfBaptism,
            childBaptisedBy,
            childConfirmed,
            childConfirmationDate,
            childPlaceOfConfirmation,
            childConfirmedBy
        };

        children.push(childObject);
    }

    let childrenDetails = {
        numberOfChildren: numberOfChildren,
        children: children
    };

    let member = {
        mainDetails: mainDetails,
        spouse: spouseDetails,
        childrenDetails: childrenDetails
    }

    console.log(member);

    let isValid = validateForm();
    if (isValid) {
        console.log("valid");
    } else {
        return;
    }

    $('#newMemberModal').modal('hide');

    $.ajax({
        url: "/api/member/add",
        method: "POST",
        data: JSON.stringify(member),
        contentType: "application/json",
        dataType: "json",
        success: function (response) {
            console.log("Data successfully sent to the server!");
            console.log(response);
            if (response.response === 'Member added Successfully') {
                Swal.fire({
                    title: 'success',
                    text: response.response,
                    icon: 'success',
                }).then((result) => {
                    if (result.isConfirmed) {
                        window.location.reload();
                    }
                });
            } else {
                Swal.fire({
                    title: 'error',
                    text: response.response,
                    icon: 'error',
                });
            }
        },
        error: function (xhr, status, error) {
            Swal.fire({
                title: 'error',
                text: error.response,
                icon: 'error',
            });
            console.error("Error sending data to the server:", error);
        },
    });

}

function updateMember(event) {
    event.preventDefault();

    let id = $('#editModal input[name="id"]').val();
    let image = $('#editModal input[name="imageUrl"]').val();
    let first = $('#editModal input[name="fName"]').val();
    let second = $('#editModal input[name="mName"]').val();
    let last = $('#editModal input[name="lName"]').val();
    let dob = $('#editModal input[name="dob"]').val();

    let gender = $('#editModal select[name="gender"]').val();
    let membershipDuration = $('#editModal select[name="membershipDuration"]').val();
    let enrolmentDate = $('#editModal input[name="enrolmentDate"]').val();
    let enrolmentPlace = $('#editModal input[name="enrolmentPlace"]').val();
    let memberGroup = $('#editModal select[name="memberGroup"]').val();
    let phone = $('#editModal input[name="phone"]').val();
    let email = $('#editModal input[name="email"]').val();

    let address = $('#editModal input[name="address"]').val();
    let residence = $('#editModal input[name="residence"]').val();
    let zone = $('#editModal select[name="zone"]').val();
    let preferredService = $('#editModal select[name="preferredService"]').val();

    let profession = $('#editModal input[name="profession"]').val();
    let occupation = $('#editModal input[name="occupation"]').val();
    let employer = $('#editModal input[name="employer"]').val();

    let baptismDate = $('#editModal input[name="baptismDate"]').val();
    let placeOfBaptism = $('#editModal input[name="baptismPlace"]').val();
    let baptisedBy = $('#editModal input[name="baptisedBy"]').val();

    let confirmationDate = $('#editModal input[name="confirmationDate"]').val();
    let placeOfConfirmation = $('#editModal input[name="confirmationPlace"]').val();
    let confirmedBy = $('#editModal input[name="confirmedBy"]').val();

    let mainDetails = {
        id,
        image: image,
        first: first,
        second: second,
        last: last,
        dob: dob,
        gender: gender,
        membershipDuration, membershipDuration,
        isMember: isMember,
        memberGroup: memberGroup,
        enrolmentPlace,
        enrolmentDate,
        phone: phone,
        email: email,
        address: address,
        residence: residence,
        zone: zone,
        preferredService: preferredService,
        profession: profession,
        occupation: occupation,
        employer: employer,
        baptised: baptised,
        baptismDate: baptismDate,
        placeOfBaptism: placeOfBaptism,
        baptisedBy: baptisedBy,
        confirmed: confirmed,
        confirmationDate: confirmationDate,
        placeOfConfirmation: placeOfConfirmation,
        confirmedBy: confirmedBy
    };

    let maritalStatus = $('#editModal select[name="maritalStatus"]').val();
    let marriageType = $('#editModal select[name="marriageType"]').val();
    let weddingDate = $('#editModal input[name="weddingDate"]').val();
    let weddingPlace = $('#editModal input[name="weddingPlace"]').val();

    let spouseId = $('#editModal #spouseId').val();
    let spouseImage = $('#editModal #spouseImageUrledit').val();
    let spouseFName;
    let spouseSecond;
    let spouseLast;
    if (maritalStatus === 'Married') {
        spouseFName = $('#editModal #spouseDetailsedit input[name="spouseFName"]').val();
        spouseSecond = $('#editModal #spouseDetailsedit input[name="spouseMName"]').val();
        spouseLast = $('#editModal #spouseDetailsedit input[name="spouseLName"]').val();
    } else if (maritalStatus === 'Widow' || maritalStatus === 'Widower') {
        spouseFName = $('#editModal #deceasedSpouseDetailsEdit input[name="spouseFName"]').val();
        spouseSecond = $('#editModal #deceasedSpouseDetailsEdit input[name="spouseMName"]').val();
        spouseLast = $('#editModal #deceasedSpouseDetailsEdit input[name="spouseLName"]').val();
    }
    let spouseDob = $('#editModal input[name="spouseDob"]').val();

    let spouseGender = $('#editModal select[name="spouseGender"]').val();
    let spouseMemberGroup = $('#editModal select[name="spouseMemberGroup"]').val();
    let spousePhone = $('#editModal input[name="spousePhone"]').val();
    let spouseEmail = $('#editModal input[name="spouseEmail"]').val();

    let spouseAddress = $('#editModal input[name="spouseAddress"]').val();
    let spouseResidence = $('#editModal input[name="spouseResidence"]').val();
    let spouseZone = $('#editModal select[name="spouseZone"]').val();
    let spousePreferredService = $('#editModal select[name="spousePreferredService"]').val();

    let spouseProfession = $('#editModal input[name="spouseProfession"]').val();
    let spouseOccupation = $('#editModal input[name="spouseOccupation"]').val();
    let spouseEmployer = $('#editModal input[name="spouseEmployer"]').val();

    let spouseBaptismDate = $('#editModal input[name="spouseBaptismDate"]').val();
    let spousePlaceOfBaptism = $('#editModal input[name="spouseBaptismPlace"]').val();
    let spouseBaptisedBy = $('#editModal input[name="spouseBaptisedBy"]').val();

    let spouseConfirmationDate = $('#editModal input[name="spouseConfirmationDate"]').val();
    let spousePlaceOfConfirmation = $('#editModal input[name="spouseConfirmationPlace"]').val();
    let spouseConfirmedBy = $('#editModal input[name="spouseConfirmedBy"]').val();

    let spouseDetails = {
        spouseId,
        maritalStatus: maritalStatus,
        marriageType: marriageType,
        weddingDate: weddingDate,
        weddingPlace: weddingPlace,
        spouseImage: spouseImage,
        spouseFName: spouseFName,
        spouseSecond: spouseSecond,
        spouseLast: spouseLast,
        spouseDob: spouseDob,
        spouseGender: spouseGender,
        spouseMemberGroup: spouseMemberGroup,
        spousePhone: spousePhone,
        spouseEmail: spouseEmail,
        spouseAddress: spouseAddress,
        spouseResidence: spouseResidence,
        spouseZone: spouseZone,
        spousePreferredService: spousePreferredService,
        spouseProfession: spouseProfession,
        spouseOccupation: spouseOccupation,
        spouseEmployer: spouseEmployer,
        spouseBaptised: spouseBaptised,
        spouseBaptismDate: spouseBaptismDate,
        spousePlaceOfBaptism: spousePlaceOfBaptism,
        spouseBaptisedBy: spouseBaptisedBy,
        spouseConfirmed: spouseConfirmed,
        spouseConfirmationDate: spouseConfirmationDate,
        spousePlaceOfConfirmation: spousePlaceOfConfirmation,
        spouseConfirmedBy: spouseConfirmedBy
    };

    let numberOfChildren = $('#numberOfChildrenedit').val();

    let children = [];

    for (let i = 1; i <= numberOfChildren; i++) {
        let childId = $(`input[name="child${i}Id"]`).val();
        let childImage = $('#child' + i + 'ImageUrl').val();
        let childFName = $(`input[name="child${i}FName"]`).val();
        let childMName = $(`input[name="child${i}MName"]`).val();
        let childLName = $(`input[name="child${i}LName"]`).val();
        let childDob = $(`input[name="child${i}Dob"]`).val();
        let childGender = $(`select[name="child${i}Gender"]`).val();
        let childMemberGroup = $(`select[name="child${i}MemberGroup"]`).val();
        let childPhone = $(`input[name="child${i}Phone"]`).val();
        let childEmail = $(`input[name="child${i}Email"]`).val();
        let childAddress = $(`input[name="child${i}Address"]`).val();
        let childResidence = $(`input[name="child${i}Residence"]`).val();
        let childZone = $(`select[name="child${i}Zone"]`).val();
        let childPreferredService = $(`select[name="child${i}PreferredService"]`).val();
        let childProfession = $(`input[name="child${i}Profession"]`).val();
        let childOccupation = $(`input[name="child${i}Occupation"]`).val();
        let childEmployer = $(`input[name="child${i}Employer"]`).val();
        let childBaptised = $(`input[name="child${i}Baptised"]`).is(':checked');
        let childBaptismDate = $(`input[name="child${i}BaptismDate"]`).val();
        let childPlaceOfBaptism = $(`input[name="child${i}BaptismPlace"]`).val();
        let childBaptisedBy = $(`input[name="child${i}BaptisedBy"]`).val();
        let childConfirmed = $(`input[name="child${i}Confirmed"]`).is(':checked');
        let childConfirmationDate = $(`input[name="child${i}ConfirmationDate"]`).val();
        let childPlaceOfConfirmation = $(`input[name="child${i}ConfirmationPlace"]`).val();
        let childConfirmedBy = $(`input[name="child${i}ConfirmedBy"]`).val();

        let childObject = {
            childId,
            childImage,
            childFName,
            childMName,
            childLName,
            childDob,
            childGender,
            childMemberGroup,
            childPhone,
            childEmail,
            childAddress,
            childResidence,
            childZone,
            childPreferredService,
            childProfession,
            childOccupation,
            childEmployer,
            childBaptised,
            childBaptismDate,
            childPlaceOfBaptism,
            childBaptisedBy,
            childConfirmed,
            childConfirmationDate,
            childPlaceOfConfirmation,
            childConfirmedBy
        };

        children.push(childObject);
    }

    let childrenDetails = {
        numberOfChildren: numberOfChildren,
        children: children
    };

    let updateMember = {
        mainDetails: mainDetails,
        spouse: spouseDetails,
        childrenDetails: childrenDetails
    }

    console.log(updateMember);

    let isValid = validateForm();
    if (isValid) {
        console.log("valid");
    } else {
        return;
    }

    $('#editModal').modal('hide');

    $.ajax({
        url: "/api/member/update",
        method: "POST",
        data: JSON.stringify(updateMember),
        contentType: "application/json",
        dataType: "json",
        success: function (response) {
            console.log(response);
            if (response.response === 'Member updated successfully') {
                Swal.fire({
                    title: 'success',
                    text: response.response,
                    icon: 'success',
                }).then((result) => {
                    if (result.isConfirmed) {
                        window.location.reload();
                    }
                });
            } else {
                Swal.fire({
                    title: 'error',
                    text: response.response,
                    icon: 'error',
                });
            }
        },
        error: function (xhr, status, error) {
            Swal.fire({
                title: 'error',
                text: error.response,
                icon: 'error',
            });
            console.error("Error sending data to the server:", error);
        },
    });

}

function refreshBalance() {
    fetch('/sms/refreshBalance')
        .then(response => response.json())
        .then(data => {
            console.log(data);
            if (data.balance === "Check your internet connection and try again") {
                $('#infoModal .modal-body').empty();
                $('#infoModal .modal-body').append("<p>" + data.balance + "</p>");
                $('#infoModal').modal('show');
            }
            $('#accountBalanceDiv').removeClass('hidden')
            $('#accountBalanceDiv #walletBalance').text("Wallet Balance: " + data.balance);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function calculateSmsPerPerson() {
    let smsPerPerson;
    var query = $('#message').val();
    $.post('/sms/countSmsPerPerson', {query: query}, function (results) {
        console.log(results);
        localStorage.setItem("smsPerPerson", results.smsPerPerson);
        smsPerPerson = results.smsPerPerson;
    });

    return smsPerPerson;
}

function getPhoneNumbers(query) {
    console.log(query);
    fetch('/sms/getPhoneNumbers?query=' + encodeURIComponent(query), {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            var phoneNumbersText = data.join(',');
            let count = data.length;
            $('#totalRecipients').text(count);
            $('#phoneNumbers').val(phoneNumbersText);
            localStorage.setItem("totalRecipients", count);
        })
        .catch(error => {
            console.error(error);
        });
}

function confirmThings() {
    var recipientNumbers = $('#phoneNumbers').val();
    var message = $('#message').val();
    var totalRecipients = localStorage.getItem("totalRecipients");
    var smsPerPerson = localStorage.getItem("smsPerPerson");

    if (!recipientNumbers || !message) {
        Swal.fire('Please enter recipient numbers and message')
        return;
    }

    html = '<h6>Sms per person: </h6> <span id="smsPerPerson">' + smsPerPerson + '</span>\n' +
        '<h6>Total Recipients: </h6> <span id="totalRecipients">' + totalRecipients + '</span>';

    Swal.fire({
        title: 'Do you want to proceed?',
        icon: 'question',
        html: html,
        showCancelButton: true,
        confirmButtonText: 'Proceed',
    }).then((result) => {
        if (result.isConfirmed) {
            sendBulkSMS(recipientNumbers, message, totalRecipients, smsPerPerson);
        } else if (result.isDenied) {
            Swal.fire('Cancelled', '', 'info')
        }
    })
}

function sendBulkSMS(recipientNumbers, message, totalRecipients, smsPerPerson) {
    var data = {
        recipientNumbers: recipientNumbers,
        message: message
    };
    console.log(data);

    var jsonData = JSON.stringify(data);

    var sse = new SSE("/sms/sendBulkSMS", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        payload: jsonData
    });

    let progressAlert;
    sse.addEventListener('open', function () {
        console.log("SSE connection opened");
        let sent = 0;
        let totalMessages = smsPerPerson * totalRecipients;
        let progress = "sent " + sent + "/" + totalMessages + " messages";

        progressAlert = Swal.fire({
            title: 'Sending Messages',
            text: progress,
            icon: 'info',
        });
    });

    sse.addEventListener('sent', function (event) {
        var sent = JSON.parse(event.data);
        let totalMessages = smsPerPerson * totalRecipients;
        console.log(sent);
        console.log("SSE connection opened");
        let progress = "sent " + sent + "/" + totalMessages + " messages";
        progressAlert.update({
            text: progress
        })
    });

    sse.addEventListener('internet', function (event) {
        var response = JSON.parse(event.data);
        Swal.fire({
            title: 'error',
            text: response,
            icon: 'error',
        });
    });

    sse.addEventListener('complete', function (event) {
        var response = JSON.parse(event.data);
        progressAlert.update({
            title: response.message,
            html:
                '<p>Sent:  ' + response.sent + '</p>' +
                '<p>Failed:  ' + response.failed + '</p>',
            icon: 'success',
        });
    });

    sse.addEventListener('message', function (event) {
        var eventData = JSON.parse(event.data);
        console.log(eventData);
        if (eventData.name === "progress") {
            console.log(eventData.data);
            $('#sent').text(eventData.data);
        } else if (eventData.name === "complete") {
            console.log(eventData.data);
            $('#infoModal .modal-body').empty();
            $('#infoModal .modal-body').append("<p>" + eventData.data + "</p>");
            $('#infoModal').modal('show');
            $('#sent').text(eventData.data);
            sse.close();
        } else if (eventData.name === "error") {
            console.error(eventData.data);
            /*Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: eventData.data
            })*/
        }
    });

    sse.addEventListener('error', function (event) {
        console.error("SSE connection error:", event);
        /*$('#infoModal .modal-body').empty();
        $('#infoModal .modal-body').append("<p>" + event.data + "</p>");
        $('#infoModal').modal('show');*/
    });

    sse.stream();
}


/*function sendBulkSMS() {
    // Validate recipient numbers and message fields
    var recipientNumbers = $('#phoneNumbers').val();
    var message = $('#message').val();

    if (!recipientNumbers || !message) {
        // Show error message if required fields are empty
        alert("Please enter recipient numbers and message");
        return;
    }

    // Create payload data
    var data = {
        recipientNumbers: recipientNumbers,
        message: message
    };
    console.log(data);

    fetch("/sms/sendBulkSMS", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            console.log(response);
            response.text();
            console.log(response.t)
        })
        .then(message => {
            console.log(message);
            if (message === 'Bulk SMS sent successfully') {
                $('#infoModal .modal-body').empty();
                $('#infoModal .modal-body').append("<p>"+message+"</p>");
                $('#infoModal').modal('show');
            }
        })
        .catch(error => {
            console.error(error);
            $('#infoModal .modal-body').empty();
            $('#infoModal .modal-body').append("<p>Check your internet connection and try again</p>");
            $('#infoModal').modal('show');
        });
}*/

function addVoucher(event) {
    event.preventDefault();
    let modal = $('#newVoucherModal');

    let dateField = modal.find('#date');
    let activityField = modal.find('#activity');
    let payeeField = modal.find('#payee');
    let detailsField = modal.find('#details');
    let bankField = modal.find('#bank');
    let transRefField = modal.find('#transRef');
    let withholdingField = modal.find('#withholding');
    let vatField = modal.find('#vat');
    let profFeesField = modal.find('#profFees');

    let errorMessages = [];
    let errorFields = [dateField, activityField, payeeField, detailsField, bankField, withholdingField, vatField, profFeesField];

    if (!dateField.val()) {
        errorMessages.push('Date is required.');
    }
    if (!activityField.val()) {
        errorMessages.push('Activity is required.');
    }

    if (!payeeField.val()) {
        errorMessages.push('Payee is required.');
    }

    if (!detailsField.val()) {
        errorMessages.push('Details are required.');
    }

    if (!bankField.val()) {
        errorMessages.push('Bank is required.');
    }

    if (!withholdingField.val()) {
        errorMessages.push('Withholding is required.');
    }

    if (!vatField.val()) {
        errorMessages.push('VAT is required.');
    }

    if (!profFeesField.val()) {
        errorMessages.push('Professional Fees are required.');
    }

    errorFields.forEach(function (field) {
        field.removeClass('is-invalid');
    });

    if (errorMessages.length > 0) {
        errorFields.forEach(function (field, index) {
            if (errorMessages[index]) {
                field.addClass('is-invalid');
                field.next('.invalid-feedback').text(errorMessages[index]);
            }
        });
        return;
    }

    modal.modal('hide');
    let date = modal.find('#date').val();
    let activity = modal.find('#activity').val();
    let payee = modal.find('#payee').val();
    let details = modal.find('#details').val();
    let bank = modal.find('#bank').val();
    let transRef = modal.find('#transRef').val();
    let withholding = modal.find('#withholding').val();
    let vat = modal.find('#vat').val();
    let profFees = modal.find('#profFees').val();
    let voucherTableData = createDataArray();
    let amountBeforeTax = localStorage.getItem("totalBeforeTax");
    let totalPayable = localStorage.getItem("totalPayable");

    let myObject = {
        date: date,
        activity: activity,
        payee: payee,
        details: details,
        bank: bank,
        transRef: transRef,
        withholding: withholding,
        vat: vat,
        profFees: profFees,
        amountBeforeTax: amountBeforeTax,
        totalPayable: totalPayable,
        voucherTableData: voucherTableData
    };

    let jsonData = JSON.stringify(myObject);

    $.ajax({
        url: "/finance/voucher/add",
        method: "POST",
        data: jsonData,
        contentType: "application/json",
        dataType: "json",
        success: function (response) {
            console.log("Data successfully sent to the server!");
            console.log(response);
            if (response.response === 'Data saved successfully.') {
                Swal.fire({
                    title: 'success',
                    text: response.response,
                    icon: 'success',
                }).then((result) => {
                    if (result.isConfirmed) {
                        window.location.reload();
                    }
                });
            } else {
                Swal.fire({
                    title: 'error',
                    text: response.response,
                    icon: 'error',
                });
            }
        },
        error: function (xhr, status, error) {
            Swal.fire({
                title: 'error',
                text: error.response,
                icon: 'error',
            });
            console.error("Error sending data to the server:", error);
        },
    });

}

function addReceipt(event) {
    event.preventDefault();
    let modal = $('#newReceiptModal');
    let date = modal.find('#date').val();
    let receivedFrom = modal.find('#payee').val();
    let details = modal.find('#details').val();
    let bank = modal.find('#bank').val();
    let transRef = modal.find('#transRef').val();
    let receiptTableRow = createReceiptRow();

    let myObject = {
        date: date,
        receivedFrom: receivedFrom,
        details: details,
        bank: bank,
        transRef: transRef,
        receiptTableRowList: receiptTableRow
    };
    console.log(myObject);

    let jsonData = JSON.stringify(myObject);

    let isValid = validateForm();
    if (isValid) {
        console.log("valid");
    } else {
        return;
    }

    modal.modal('hide');

    $.ajax({
        url: "/finance/receipt/add",
        method: "POST",
        data: jsonData,
        contentType: "application/json",
        dataType: "json",
        success: function (response) {
            console.log(response);
            if (response.response === 'Data saved successfully.') {
                Swal.fire({
                    title: 'success',
                    text: response.response,
                    icon: 'success',
                }).then((result) => {
                    if (result.isConfirmed) {
                        window.location.reload();
                    }
                });
            } else {
                Swal.fire({
                    title: 'error',
                    text: response.response,
                    icon: 'error',
                });
            }
        },
        error: function (xhr, status, error) {
            Swal.fire({
                title: 'error',
                text: error.response,
                icon: 'error',
            });
            console.error("Error sending data to the server:", error);
        },
    });

}

function updateReceipt(event) {
    event.preventDefault();
    let modal = $('#editModal');
    modal.modal('hide');
    let referenceNumber = modal.find("#id").val();
    let date = modal.find('#editDate').val();
    let receivedFrom = modal.find('#editPayee').val();
    let details = modal.find('#editDetails').val();
    let bank = modal.find('#editBank').val();
    let transRef = modal.find('#editTransRef').val();
    let receiptTableRow = createUpdateReceiptRow();

    let myObject = {
        referenceNumber: referenceNumber,
        date: date,
        receivedFrom: receivedFrom,
        details: details,
        bank: bank,
        transRef: transRef,
        receiptTableRowList: receiptTableRow
    };
    console.log(myObject);

    let jsonData = JSON.stringify(myObject);

    $.ajax({
        url: "/finance/receipt/update",
        method: "POST",
        data: jsonData,
        contentType: "application/json",
        dataType: "json",
        success: function (response) {
            console.log(response);
            if (response.response === 'Data updated successfully.') {
                Swal.fire({
                    title: 'success',
                    text: response.response,
                    icon: 'success',
                }).then((result) => {
                    if (result.isConfirmed) {
                        window.location.reload();
                    }
                });
            } else {
                Swal.fire({
                    title: 'error',
                    text: response.response,
                    icon: 'error',
                });
            }
        },
        error: function (xhr, status, error) {
            Swal.fire({
                title: 'error',
                text: error.response,
                icon: 'error',
            });
            console.error("Error sending data to the server:", error);
        },
    });

}

function updateVoucher(event) {
    event.preventDefault();
    let modal = $('#editModal');
    modal.modal('hide');
    let id = modal.find('input[name="id"]').val();
    let date = modal.find('#editDate').val();
    let activity = modal.find('#editActivity').val();
    let payee = modal.find('#editPayee').val();
    let details = modal.find('#editDetails').val();
    let bank = modal.find('#editBank').val();
    let transRef = modal.find('#editTransRef').val();
    let withholding = modal.find('#editWithholding').val();
    let vat = modal.find('#editVat').val();
    let profFees = modal.find('#editProfFees').val();
    let voucherTableData = createDataArrayEdit();
    let amountBeforeTax = localStorage.getItem("totalBeforeTax");
    let totalPayable = localStorage.getItem("totalPayable");

    let myObject = {
        pv: id,
        date: date,
        activity: activity,
        payee: payee,
        details: details,
        bank: bank,
        transRef: transRef,
        withholding: withholding,
        vat: vat,
        profFees: profFees,
        amountBeforeTax: amountBeforeTax,
        totalPayable: totalPayable,
        voucherTableData: voucherTableData
    };

    console.log(myObject);

    let jsonData = JSON.stringify(myObject);

    $.ajax({
        url: "/finance/voucher/update",
        method: "POST",
        data: jsonData,
        contentType: "application/json",
        dataType: "json",
        success: function (response) {
            if (response.response === 'Data updated successfully.') {
                Swal.fire({
                    title: 'success',
                    text: response.response,
                    icon: 'success',
                }).then((result) => {
                    if (result.isConfirmed) {
                        window.location.reload();
                    }
                });
            } else {
                Swal.fire({
                    title: 'error',
                    text: response.response,
                    icon: 'error',
                });
            }
        },
        error: function (xhr, status, error) {
            Swal.fire({
                title: 'error',
                text: error.response,
                icon: 'error',
            });
            console.error("Error sending data to the server:", error);
        },
    });

}

function createUpdateReceiptRow() {
    var dataArray = [];

    $("#editReceiptsTable tbody tr").each(function () {
        var transactionId = $(this).find("input[name='transactionId']").val();
        var activity = $(this).find("select[name='particulars']").val();
        var amount = parseFloat($(this).find("input[name='amount']").val());

        var dataObject = {
            transactionId: transactionId,
            activity: activity,
            amount: amount
        };

        dataArray.push(dataObject);
    });

    return dataArray;
}

function createReceiptRow() {
    var dataArray = [];

    $("#receiptsTable tbody tr").each(function () {
        var activity = $(this).find("select[name='particulars']").val();
        var amount = parseFloat($(this).find("input[name='amount']").val());

        var dataObject = {
            activity: activity,
            amount: amount
        };

        dataArray.push(dataObject);
    });

    return dataArray;
}

function createDataArrayEdit() {
    var dataArray = [];

    $("#editVouchersTable tbody tr").each(function () {
        var particulars = $(this).find("input[name='particulars']").val();
        var quantity = parseInt($(this).find("input[name='quantity']").val());
        var rate = parseFloat($(this).find("input[name='rate']").val());
        var amount = parseFloat($(this).find("input[name='amount']").val());

        var dataObject = {
            particulars: particulars,
            quantity: quantity,
            rate: rate,
            amount: amount
        };

        dataArray.push(dataObject);
    });

    return dataArray;
}

function createDataArray() {
    var dataArray = [];

    $("#vouchersTable tbody tr").each(function () {

        /*let particularsField = $('#particulars');
        let quantityField = $('#quantity');
        let rateField = $('#rate');

        let errorMessages = [];
        let errorFields = [particularsField, quantityField, rateField];

        if (!particularsField.val()) {
            errorMessages.push('Particulars is required.');
        }
        if (!quantityField.val()) {
            errorMessages.push('Quanity is required.');
        }

        if (!rateField.val()) {
            errorMessages.push('Rate is required.');
        }

        errorFields.forEach(function (field) {
            field.removeClass('is-invalid');
        });

        if (errorMessages.length > 0) {
            errorFields.forEach(function (field, index) {
                if (errorMessages[index]) {
                    field.addClass('is-invalid');
                    field.next('.invalid-feedback').text(errorMessages[index]);
                }
            });
            return;
        }*/


        var particulars = $(this).find("input[name='particulars']").val();
        var quantity = parseInt($(this).find("input[name='quantity']").val());
        var rate = parseFloat($(this).find("input[name='rate']").val());
        var amount = parseFloat($(this).find("input[name='amount']").val());

        var dataObject = {
            particulars: particulars,
            quantity: quantity,
            rate: rate,
            amount: amount
        };

        dataArray.push(dataObject);
    });

    return dataArray;
}


function populate(json) {
    $('#member .personId').text(json.member.id);
    $('#member .personName').text(json.member.fName);
    $('#member .memberImage').attr('src', json.member.image);
    $('#member input[name="profession"]').val(json.member.profession);
    $('#member input[name="occupation"]').val(json.member.occupation);
    $('#member input[name="employer"]').val(json.member.employer);
    $('#member input[name="residence"]').val(json.member.residence);
    $('#member input[name="zone"]').val(json.member.zone.zone);
    $('#member input[name="preferredService"]').val(json.member.preferredService.service);
    $('#member input[name="memberGroup"]').val(json.member.memberGroup.group);
    $('#member input[name="firstName"]').val(json.member.fName);
    $('#member input[name="middleName"]').val(json.member.mName);
    $('#member input[name="lastName"]').val(json.member.lName);
    $('#member input[name="address"]').val(json.member.address);
    $('#member input[name="gender"]').val(json.member.gender);
    $('#member input[name="phone"]').val(json.member.phone);
    $('#member input[name="email"]').val(json.member.email);
    $('#member input[name="dateOfBirth"]').val(json.member.dob);
    console.log($('#member input[name="dateOfBirth"]'));
    console.log('#member input[name="dateOfBirth"]')

    $('#member input[name="confirmationStatus"]').val(json.member.confirmationStatus);
    if (json.member.confirmationStatus === 'true' || json.member.confirmationStatus === 'yes') {
        $('#member input[name="confirmedBy"]').val(json.member.confirmationDetails.confirmedBy);
        $('#member input[name="confirmationDate"]').val(json.member.confirmationDetails.confirmationDate);
        $('#member input[name="confirmationPlace"]').val(json.member.confirmationDetails.confirmationPlace);
    }

    $('#member input[name="baptismStatus"]').val(json.member.baptismStatus);
    if (json.member.baptismStatus === 'true' || json.member.baptismStatus === 'yes') {
        $('#member input[name="baptizedBy"]').val(json.member.baptismDetails.baptisedBy);
        $('#member input[name="baptismDate"]').val(json.member.baptismDetails.baptismPlace);
        $('#member input[name="baptismPlace"]').val(json.member.baptismDetails.baptismDate);
    }
    $('#member input[name="maritalStatus"]').val(json.member.maritalStatus);

    if (json.member.maritalStatus === 'Married') {
        $('#spouse').removeClass('hidden');
        $('#spouse .personId').text(json.spouse.id);
        $('#spouse .personName').text(json.spouse.fName);
        $('#spouse .memberImage').attr('src', json.spouse.image);
        $('#spouse input[name="profession"]').val(json.spouse.profession);
        $('#spouse input[name="occupation"]').val(json.spouse.occupation);
        $('#spouse input[name="employer"]').val(json.spouse.employer);
        $('#spouse input[name="residence"]').val(json.spouse.residence);
        $('#spouse input[name="zone"]').val(json.spouse.zone.zone);
        $('#spouse input[name="preferredService"]').val(json.spouse.preferredService.service);
        $('#spouse input[name="memberGroup"]').val(json.spouse.memberGroup.group);
        $('#spouse input[name="firstName"]').val(json.spouse.fName);
        $('#spouse input[name="middleName"]').val(json.spouse.mName);
        $('#spouse input[name="lastName"]').val(json.spouse.lName);
        $('#spouse input[name="address"]').val(json.spouse.address);
        $('#spouse input[name="gender"]').val(json.spouse.gender);
        $('#spouse input[name="phone"]').val(json.spouse.phone);
        $('#spouse input[name="email"]').val(json.spouse.email);
        $('#spouse input[name="dateOfBirth"]').val(json.spouse.dob);

        $('#spouse input[name="confirmationStatus"]').val(json.spouse.confirmationStatus);
        if (json.member.confirmationStatus === 'true' || json.member.confirmationStatus === 'yes') {
            $('#spouse input[name="confirmedBy"]').val(json.spouse.confirmationDetails.confirmedBy);
            $('#spouse input[name="confirmationDate"]').val(json.spouse.confirmationDetails.confirmationDate);
            $('#spouse input[name="confirmationPlace"]').val(json.spouse.confirmationDetails.confirmationPlace);
        }

        $('#spouse input[name="baptismStatus"]').val(json.spouse.baptismStatus);
        if (json.spouse.baptismStatus === 'true' || json.spouse.baptismStatus === 'yes') {
            $('#spouse input[name="baptizedBy"]').val(json.spouse.baptismDetails.baptisedBy);
            $('#spouse input[name="baptismDate"]').val(json.spouse.baptismDetails.baptismPlace);
            $('#spouse input[name="baptismPlace"]').val(json.spouse.baptismDetails.baptismPlace);
        }

        $('#spouse input[name="maritalStatus"]').val(json.spouse.marriage.maritalStatus);
        if (json.member.maritalStatus === 'Married') {
            $('#spouse input[name="marriageType"]').val(json.spouse.marriage.marriageType);
            $('#spouse input[name="marriageDate"]').val(json.spouse.marriage.weddingDate);
            $('#spouse input[name="marriagePlace"]').val(json.spouse.marriage.weddingDate);
        }
        $('#spouse input[name="spouseId"]').val(json.spouse.spouseId);
        $('#spouse input[name="spouseDob"]').val(json.spouse.spouseDob);
        $('#spouse input[name="spouseResidence"]').val(json.spouse.spouseResidence);
    } else {
        $('#spouse').addClass('hidden');
    }

    $.each(json.childrenAsFather, function (index, child) {

        console.log(index);
        let childRow = '<div id="child' + index + '" class="row gx-4 mb-2">\n' +
            '                <div class="col-auto">\n' +
            '                    <div class="avatar avatar-xl position-relative">\n' +
            '                        <img class="memberImage" src="static/thika/MemberImages/WIN_20230508_16_43_49_Pro.jpg"\n' +
            '                             alt="Profile Image">\n' +
            '                    </div>\n' +
            '                </div>\n' +
            '                <div class="col-auto my-auto">\n' +
            '                    <h3>Child Profile</h3>\n' +
            '                    <div class="h-100">\n' +
            '                        <h5 class="mb-1 personName"></h5>\n' +
            '                        <p class="mb-0 font-weight-normal text-sm">\n' +
            '                            <strong>ID:</strong> <span class="personId"></span>\n' +
            '                        </p>\n' +
            '                    </div>\n' +
            '                </div>\n' +
            '                <div class="row">\n' +
            '                    <div class="col-12 col-xl-4">\n' +
            '                        <div class="card card-plain h-100"> Child Information\n' +
            '                            <div class="card-body p-3">\n' +
            '                                <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                    <label class="form-label">First Name</label>\n' +
            '                                    <input type="text" name="firstName" class="form-control" disabled>\n' +
            '                                </div>\n' +
            '                                <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                    <label class="form-label">Middle Name</label>\n' +
            '                                    <input type="text" name="middleName" class="form-control" disabled>\n' +
            '                                </div>\n' +
            '                                <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                    <label class="form-label">Last Name</label>\n' +
            '                                    <input type="text" name="lastName" class="form-control" disabled>\n' +
            '                                </div>\n' +
            '                                <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                    <label class="form-label">Address</label>\n' +
            '                                    <input type="text" name="address" class="form-control" disabled>\n' +
            '                                </div>\n' +
            '                                <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                    <label class="form-label">Gender</label>\n' +
            '                                    <input type="text" name="gender" class="form-control" disabled>\n' +
            '                                </div>\n' +
            '                                <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                    <label class="form-label">Phone</label>\n' +
            '                                    <input type="text" name="phone" class="form-control" disabled>\n' +
            '                                </div>\n' +
            '                                <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                    <label class="form-label">Email</label>\n' +
            '                                    <input type="text" name="email" class="form-control" disabled>\n' +
            '                                </div>\n' +
            '                                <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                    <label class="form-label">Date of Birth</label>\n' +
            '                                    <input type="text" name="dateOfBirth" class="form-control" disabled>\n' +
            '                                </div>\n' +
            '                                <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                    <label class="form-label">Profession</label>\n' +
            '                                    <input type="text" name="profession" class="form-control" disabled>\n' +
            '                                </div>\n' +
            '\n' +
            '                            </div>\n' +
            '                        </div>\n' +
            '                    </div>\n' +
            '                    <div class="col-12 col-xl-4">\n' +
            '                        <div class="card card-plain h-100">\n' +
            '                            <div class="row">\n' +
            '                                <div class="card-header pb-0 p-3">\n' +
            '                                    <div class="col-md-8 d-flex align-items-center"></div>\n' +
            '                                </div>\n' +
            '                            </div>\n' +
            '                            <div class="card-body p-3">\n' +
            '\n' +
            '                                <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                    <label class="form-label">Occupation</label>\n' +
            '                                    <input type="text" name="occupation" class="form-control" disabled>\n' +
            '                                </div>\n' +
            '                                <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                    <label class="form-label">Employer</label>\n' +
            '                                    <input type="text" name="employer" class="form-control" disabled>\n' +
            '                                </div>\n' +
            '                                <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                    <label class="form-label">Residence</label>\n' +
            '                                    <input type="text" name="residence" class="form-control" disabled>\n' +
            '                                </div>\n' +
            '                                <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                    <label class="form-label">Zone</label>\n' +
            '                                    <input type="text" name="zone" class="form-control" disabled>\n' +
            '                                </div>\n' +
            '                                <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                    <label class="form-label">Preferred Service</label>\n' +
            '                                    <input type="text" name="preferredService" class="form-control" disabled>\n' +
            '                                </div>\n' +
            '                                <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                    <label class="form-label">Member Group</label>\n' +
            '                                    <input type="text" name="memberGroup" class="form-control" disabled>\n' +
            '                                </div>\n' +
            '                                <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                    <label class="form-label">Confirmation Status</label>\n' +
            '                                    <input type="text" name="confirmationStatus" class="form-control" disabled>\n' +
            '                                </div>\n' +
            '                                <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                    <label class="form-label">Confirmed By</label>\n' +
            '                                    <input type="text" name="confirmedBy" class="form-control" disabled>\n' +
            '                                </div>\n' +
            '\n' +
            '                                <hr class="horizontal gray-light my-4">\n' +
            '                            </div>\n' +
            '                        </div>\n' +
            '                    </div>\n' +
            '                    <div class="col-12 col-xl-4">\n' +
            '                        <div class="card card-plain h-100">\n' +
            '                            <div class="card-header pb-0 p-3">\n' +
            '                            </div>\n' +
            '                            <div class="card-body p-3">\n' +
            '                                <div class="card-body p-3">\n' +
            '\n' +
            '\n' +
            '                                    <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                        <label class="form-label">Confirmation Date</label>\n' +
            '                                        <input type="text" name="confirmeationDate" class="form-control" disabled>\n' +
            '                                    </div>\n' +
            '                                    <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                        <label class="form-label">Confirmation Place</label>\n' +
            '                                        <input type="text" name="confirmationPlace" class="form-control" disabled>\n' +
            '                                    </div>\n' +
            '                                    <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                        <label class="form-label">Baptism Status</label>\n' +
            '                                        <input type="text" name="baptismStatus" class="form-control" disabled>\n' +
            '                                    </div>\n' +
            '                                    <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                        <label class="form-label">Baptized By</label>\n' +
            '                                        <input type="text" name="baptizedBy" class="form-control" disabled>\n' +
            '                                    </div>\n' +
            '\n' +
            '                                    <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                        <label class="form-label">Baptism Date</label>\n' +
            '                                        <input type="text" name="baptismDate" class="form-control" disabled>\n' +
            '                                    </div>\n' +
            '                                    <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                        <label class="form-label">Baptism Place</label>\n' +
            '                                        <input type="text" name="baptismPlace" class="form-control" disabled>\n' +
            '                                    </div>\n' +
            '                                    <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                        <label class="form-label">Marriage Type</label>\n' +
            '                                        <input type="text" name="marriageType" class="form-control" disabled>\n' +
            '                                    </div>\n' +
            '                                    <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                        <label class="form-label">Marriage Date</label>\n' +
            '                                        <input type="text" name="marriageDate" class="form-control" disabled>\n' +
            '                                    </div>\n' +
            '                                    <div class="input-group is-filled input-group-outline my-3">\n' +
            '                                        <label class="form-label">Marriage Place</label>\n' +
            '                                        <input type="text" name="marriagePlace" class="form-control" disabled>\n' +
            '                                    </div>\n' +
            '                                </div>\n' +
            '                            </div>\n' +
            '\n' +
            '                        </div>\n' +
            '                    </div>\n' +
            '                </div>\n' +
            '            </div>';


        $('#child' + index + ' .personId').text(child.id);
        $('#child .personName').text(child.fName);
        $('#child .memberImage').attr('src', child.image);
        $('#child' + index + ' input[name="profession"]').val(child.profession);
        $('#child' + index + ' input[name="occupation"]').val(child.occupation);
        $('#child' + index + ' input[name="employer"]').val(child.employer);
        $('#child' + index + ' input[name="residence"]').val(child.residence);
        $('#child' + index + ' input[name="zone"]').val(child.zone);
        $('#child' + index + ' input[name="preferredService"]').val(child.preferredService);
        $('#child' + index + ' input[name="memberGroup"]').val(child.memberGroup);
        $('#child' + index + ' input[name="firstName"]').val(child.fName);
        console.log($('#child' + index + ' input[name="firstName"]'));
        console.log('#child' + index + ' input[name="firstName"]');
        $('#child' + index + ' input[name="middleName"]').val(child.mName);
        $('#child' + index + ' input[name="lastName"]').val(child.lName);
        $('#child' + index + ' input[name="address"]').val(child.address);
        $('#child' + index + ' input[name="gender"]').val(child.gender);
        $('#child' + index + ' input[name="phone"]').val(child.phone);
        $('#child' + index + ' input[name="email"]').val(child.email);
        $('#child' + index + ' input[name="dateOfBirth"]').val(child.dob);
        console.log(child.dob);
        $('#child' + index + ' input[name="confirmationStatus"]').val(child.confirmationStatus);
        if (child.confirmationStatus === 'true' || child.confirmationStatus === 'yes') {
            $('#child' + index + ' input[name="confirmedBy"]').val(child.confirmationDetails.confirmedBy);
            $('#child' + index + ' input[name="confirmationDate"]').val(child.confirmationDetails.confirmationDate);
            $('#child' + index + ' input[name="confirmationPlace"]').val(child.confirmationDetails.confirmationPlace);
        }
        $('#child' + index + ' input[name="baptismStatus"]').val(child.baptismStatus);
        if (json.member.baptismStatus === 'true' || json.member.baptismStatus === 'yes') {
            $('#child' + index + ' input[name="baptizedBy"]').val(child.baptismDetails.baptisedBy);
            $('#child' + index + ' input[name="baptismDate"]').val(child.baptismDetails.baptismPlace);
            $('#child' + index + ' input[name="baptismPlace"]').val(child.baptismDetails.baptismPlace);
        }
        $('#child' + index + ' input[name="maritalStatus"]').val(child.maritalStatus);
        if (json.member.maritalStatus === 'true' || json.member.maritalStatus === 'yes') {
            $('#child' + index + ' input[name="marriageType"]').val(child.marriage.marriageType);
            $('#child' + index + ' input[name="marriageDate"]').val(child.marriage.weddingDate);
            $('#child' + index + ' input[name="marriagePlace"]').val(child.marriage.weddingPlace);
        }
        $('#child' + index + ' input[name="spouseId"]').val(child.spouseId);
        $('#child' + index + ' input[name="confirmedBy"]').val(child.confirmedBy);
        $('#child' + index + ' input[name="spouseDob"]').val(child.spouseDob);
        $('#child' + index + ' input[name="spouseResidence"]').val(child.spouseResidence);

        $(document).ready(function () {
            $('#children').append(childRow);
        })

    })
}

function emptyForm(form) {
    $(form).find('input, textarea').val('');
    $(form).find('select').prop('selectedIndex', 0);
}

function approve(url) {
    $.ajax({
        url: url,
        contentType: "application/json",
        success: function (response) {
            if (response.response === 'Success') {
                Swal.fire({
                    title: 'success',
                    text: response.response,
                    icon: 'success',
                }).then((result) => {
                    if (result.isConfirmed) {
                        window.location.reload();
                    }
                });
            } else {
                Swal.fire({
                    title: 'error',
                    text: response.response,
                    icon: 'error',
                });
            }
        },
        error: function (xhr, status, error) {
            Swal.fire({
                title: 'error',
                text: error.response,
                icon: 'error',
            });
            console.error("Error sending data to the server:", error);
        },
    });
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
    console.log(url)
    Swal.fire({
        title: "Generating report...",
        text: "Please wait",
        imageUrl: "/assets/img/ajax-loader.gif",
        showConfirmButton: false,
        allowOutsideClick: false
    });
    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        console.log(xhr.responseText)
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


$(document).ready(function () {

    $(myForm).on('keydown', function (event) {
        console.log(myForm);
        if (event.key === 'Enter') {
            console.log(myForm);
            event.preventDefault();
        }
    });

    $("#membersReportModal #category").on("change", function () {
        var selectedValue = $(this).val();

        switch (selectedValue) {
            case "all":
                console.log("All selected");
                break;
            case "group":
                $.get("/api/member/groups/api", function (data) {
                    if (data && data.length > 0) {
                        console.log(data);
                        $("#choice").prop("disabled", false);
                        $("#choice").empty();
                        $.each(data, function (index, option) {
                            $("#choice").append("<option value='" + option.id + "'>" + option.group + "</option>");
                        });
                    }
                });
                break;
            case "zone":
                $.get("/api/member/zones/api", function (data) {
                    if (data && data.length > 0) {
                        $("#choice").prop("disabled", false);
                        $("#choice").empty();
                        $.each(data, function (index, option) {
                            $("#choice").append("<option value='" + option.id + "'>" + option.zone + "</option>");
                        });
                    }
                });
                break;
            case "officials":
                $("#choice").empty();
                $("#choice").prop("disabled", true);
                break;
            case "committee":
                $.get("/api/member/committee/api", function (data) {
                    if (data && data.length > 0) {
                        $("#choice").prop("disabled", false);
                        $("#choice").empty();
                        $.each(data, function (index, option) {
                            $("#choice").append("<option value='" + option.id + "'>" + option.committee + "</option>");
                        });
                    }
                });
                break;
            case "department":
                $.get("/api/member/department/api", function (data) {
                    if (data && data.length > 0) {
                        $("#choice").prop("disabled", false);
                        $("#choice").empty();
                        $.each(data, function (index, option) {
                            $("#choice").append("<option value='" + option.id + "'>" + option.committee + "</option>");
                        });
                    }
                });
                break;
            default:
                break;
        }
    });
});

function printMembers(event) {
    event.preventDefault();
    Swal.fire({
        title: "Generating report...",
        text: "Please wait",
        imageUrl: "/assets/img/ajax-loader.gif",
        showConfirmButton: false,
        allowOutsideClick: false
    });
    $('#membersReportModal').modal('hide');
    let selectedCategory = $("#category").val();
    let choice = $('#choice').val();

    let url = "/reports/members/generate?category=" + selectedCategory + "&choice=" + choice;
    var xhr = new XMLHttpRequest();

    // Define what happens when the request is successfully completed
    xhr.onload = function () {
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

    xhr.open('GET', url, true);
    xhr.send();
}

function fetchAutoCompleteData() {
    try {

        searchitems.length = 0;

        let indexed_array = {};
        if (maintable === '#zoneMembersTable') {
            let zoneId = $('#zoneId').val();
            indexed_array = {
                getBy: 'group',
                id: zoneId
            };
        } else if (maintable === '#groupMembersTable') {
            let zoneId = $('#zoneId').val();
            indexed_array = {
                getBy: 'zone',
                id: zoneId
            };
        } else if (maintable === '#talentMembersTable') {
            let zoneId = $('#zoneId').val();
            indexed_array = {
                getBy: 'talent',
                id: zoneId
            };
        }
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


function dynamictable(data, Mydiv, url, tablee, tabletoasignnewrow, Modal, Form) {

    $(tablee).DataTable().destroy(true);

    searchitems.length = 0;

    rows_selected2.length = 0;

    tablebeingedited = tabletoasignnewrow;
    maintable = tablee;
    dataarray = data;
    myform = Form;
    modaltoopen = Modal;


    collumns = Object.keys(data[0]);

    console.log(data[0])

    Myurl = url;


    $(Mydiv).empty();


    var html = '<table  id=' + tablee.substring(1) + ' class="px-3 table table-flush">';

    html += '<thead class="thead-light">';

    html += '<tr>';


    for (var i = 0; i < collumns.length; i++) {
        if (collumns[i].toUpperCase() != "IMAGE") {
            html += '<th class="align-middle text-center"><div class="d-flex align-items-center"><p class="text-xxs font-weight-bold ms-2 mb-0">' + collumns[i].toUpperCase() + '</p></div></th>';
        }
    }


    html += '</tr>';

    html += '</thead>';


    var allEmpty = Object.keys(data[0]).every(function (key) {
        return data[0][key].length === 0
    })


    if (!allEmpty) {


        html += '<tbody>';

        html += createTableRow(Myurl, data);

        html += '</tbody>';


    }


    html += '</table>';


    $(html).appendTo(Mydiv);


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
}


function createTableRow(Myurl, data) {


    var html = '';

    for (var i = 0; i < data.length; i++) {


        html += '<tr>';


        var object = data[i];


        for (var property in object) {


            if (property == "id") {
                html += '<td class="id_event"><div class="d-flex align-items-center"><p class="text-xs font-weight-bold ms-2 mb-0">' + object[property] + '</p></div></td>';
            } else if (property.toLowerCase() == "action") {

                html += '<td>\n' +
                    '                                        <a class="btn btn-success"\n' +
                    '                                           href="/api/member/view/' + object.id + '">\n' +
                    '                                            <i class="fa fa-eye" aria-hidden="true"></i>\n' +
                    '                                        </a>\n' +
                    '                                        |<a class="btn btn-warning"\n' +
                    '                                           onclick="openEditModal(\'members\', ' + object.id + ')"><i\n' +
                    '                                                class="fa fa-pencil" aria-hidden="true"></i></a>\n' +
                    '                                        |<a class="delete-modal btn btn-danger" data-bs-toggle="modal"\n' +
                    '                                            data-bs-target="#myModaldelete"\n' +
                    '                                            data-link="/api/member/delete/' + object.id + '">\n' +
                    '                                        <i class="fa fa-trash" aria-hidden="true"></i>\n' +
                    '                                    </a>\n' +
                    '                                    </td>';

            } else if (property.toLowerCase() == "image") {
                // html +='<td class="text-xs font-weight-normal"> <img  style= "width:50px;height:50px; border-radius: 50%; " src='+object[property]+'> </td>';
            } else if (property.toLowerCase() == "name") {

                html += '<td class="text-xs font-weight-normal"><div class="d-flex align-items-center"><img src=' + object["image"] + ' class="avatar avatar-xs me-2"><a  id="btnEmployeePayslip" href="javascript:;" data-bs-target="#modalemployeepayslip" data-bs-toggle="modal" class="link-success"><p class="my-2 ms-2 text-xs fw-bold">' + object[property] + '</p></a></div></td>';
            } else {

                html += '<td class="align-middle text-capitalize text-start text-wrap"><div class="d-flex align-items-center"><p class="text-xs font-weight-normal ms-4 mb-0">' + object[property] + '</p></div></td>';

            }

        }

        html += '</tr>';


    }


    return html;
}

function reportType(receiptType) {
    type = receiptType;
}

function printDateRange(event) {
    event.preventDefault();
    Swal.fire({
        title: "Generating report...",
        text: "Please wait",
        imageUrl: "/assets/img/ajax-loader.gif",
        showConfirmButton: false,
        allowOutsideClick: false
    });
    $('#receiptsReportModal').modal('hide');
    let from = $('input[name="from"]').val();
    let to = $('input[name="to"]').val();

    let url = "/reports/" + type + "/generate?from=" + from + "&to=" + to;
    var xhr = new XMLHttpRequest();

    // Define what happens when the request is successfully completed
    xhr.onload = function () {
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

function printTrialBalance(event) {
    event.preventDefault();
    Swal.fire({
        title: "Generating report...",
        text: "Please wait",
        imageUrl: "/assets/img/ajax-loader.gif",
        showConfirmButton: false,
        allowOutsideClick: false
    });
    $('#trialBalanceModal').modal('hide');
    let activity = $('#trialBalanceModal select[name="activity"]').val();
    let groupBy = $('#trialBalanceModal select[name="groupBy"]').val();
    let date = $('#trialBalanceModal input[name="date"]').val();

    let url = "/reports/trial/generate?activity=" + activity + "&type=" + groupBy + "&date=" + date;
    console.log(url);
    var xhr = new XMLHttpRequest();

    // Define what happens when the request is successfully completed
    xhr.onload = function () {
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

function printIncomeOrExpenditureSummary(event) {
    event.preventDefault();
    Swal.fire({
        title: "Generating report...",
        text: "Please wait",
        imageUrl: "/assets/img/ajax-loader.gif",
        showConfirmButton: false,
        allowOutsideClick: false
    });
    $('#expenditureModal').modal('hide');
    let activity = $('#expenditureModal select[name="activity"]').val();
    let groupBy = $('#expenditureModal select[name="groupBy"]').val();
    let yearMonth = $('#expenditureModal select[name="year-month"]').val();
    let year = $('#expenditureModal select[name="year"]').val();
    //let groupBy = $('select[name="groupBy"]').val();

    let url = "/reports/" + type + "/generate?activity=" + activity + "&type=" + groupBy + "&year=" + yearMonth + "&yearYear=" + year;
    console.log(url);
    var xhr = new XMLHttpRequest();

    // Define what happens when the request is successfully completed
    xhr.onload = function () {
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

function printDate(event) {
    event.preventDefault();
    Swal.fire({
        title: "Generating report...",
        text: "Please wait",
        imageUrl: "/assets/img/ajax-loader.gif",
        showConfirmButton: false,
        allowOutsideClick: false
    });
    $('#reconciliationReportModal').modal('hide');
    let year = $('#reconciliationReportModal #year').val();
    let month = $('#reconciliationReportModal #month').val();
    let activity = $('#reconciliationReportModal select[name="activity"]').val();

    console.log(type);

    let url = "/reports/" + type + "/generate?year=" + year + "&month=" + month + "&activity=" + activity;
    var xhr = new XMLHttpRequest();
    // Define what happens when the request is successfully completed
    xhr.onload = function () {
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

function printBankReconciliation(event) {
    event.preventDefault();
    Swal.fire({
        title: "Generating report...",
        text: "Please wait",
        imageUrl: "/assets/img/ajax-loader.gif",
        showConfirmButton: false,
        allowOutsideClick: false
    });
    $('#bankReconciliationReportModal').modal('hide');
    const form = document.getElementById("bankReconciliationForm");
    const formData = new FormData(form);

    const url = "/reports/" + type + "/generate";
    const params = new URLSearchParams(formData).toString();
    const completeUrl = `${url}?${params}`;


    var xhr = new XMLHttpRequest();

    // Define what happens when the request is successfully completed
    xhr.onload = function () {
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
    xhr.open('GET', completeUrl, true);
    xhr.send();
}

/* ----*****-----Reports -----*****----*/

/*
function login() {
    // Serialize the form data as JSON
    const formData = $('#loginForm').serializeArray();
    const requestBody = {};
    $.each(formData, function (index, field) {
        requestBody[field.name] = field.value;
    });

    // Perform an HTTP POST request to the server
    $.ajax({
        url: '/api/auth/sign-in', // Replace with the actual API endpoint on your server
        type: 'POST',
        contentType: 'application/json', // Set the content type to JSON
        data: JSON.stringify(requestBody),
        dataType: 'json', // Set the data type you expect in the response
        success: function (response) {
            // Handle successful login here (e.g., redirect or show success message)
            console.log('Login successful!');
            console.log(response);
            localStorage.setItem('accessToken', response.token);
            window.location.href = response.redirectUrl;
        },
        error: function (error) {
            // Handle login failure here (e.g., show error message)
            console.error('Login failed:', error.responseJSON);
        }
    });

    function sendPostRequest(endpoint, requestBody) {
        // Get the token from localStorage
        const token = localStorage.getItem('accessToken');

        // Prepare the request headers
        const headers = {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        };

        // Perform the authenticated request
        fetch(endpoint, {
            method: 'POST',
            headers: headers,
            body: JSON.stringify(requestBody)
        })
            .then(response => {
                return {response: response};
            })
            .catch(error => {
                return {error: error};
            });
    }

    function sendGetRequest(endpoint) {
        console.log(endpoint);
        // Get the token from localStorage
        const token = localStorage.getItem('accessToken');

        // Prepare the request headers
        const headers = {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        };

        // Perform the authenticated request
        fetch(endpoint, {
            method: 'GET',
            headers: headers
        })
            .then(response => {
                return {response: response};
            })
            .catch(error => {
                return {error: error};
            });
    }


}
*/
