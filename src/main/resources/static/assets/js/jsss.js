for (let i = 0; i <= selectedValue; i++) {
    let scrrg = "<script>\n" +
        "                                                    $(document).ready(function () {\n" +
        "                                                        const editButton = $('#editChild'+i+'ImageButton');\n" +
        "                                                        const imageInput = $('#child'+i+'ImageInput');\n" +
        "                                                        const image = $('#child'+i+'Image');\n" +
        "\n" +
        "                                                        editButton.click(function () {\n" +
        "                                                            imageInput.click();\n" +
        "                                                        });\n" +
        "\n" +
        "                                                        imageInput.change(function() {\n" +
        "                                                            const selectedFile = imageInput[0].files[0];\n" +
        "                                                            image.attr('src', selectedFile);\n" +
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
        "                                                                            Swal.fire({\n" +
        "                                                                                title: \"Success\",\n" +
        "                                                                                text: \"Upload Successful\",\n" +
        "                                                                                icon: \"success\"\n" +
        "                                                                            });\n" +
        "                                                                            $('#child'+i+'ImageUrl').val(response.path);\n" +
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
        "                                                </script>";
}