$(document).ready(function () {
    // Evento de envio do formulário de upload
    $("#uploadForm").submit(function (event) {
        event.preventDefault();

        var titulo = $("#titulo").val();
        var categoria = $("#categoria").val();
        var fileToUpload = $("#fileToUpload")[0].files[0];

        // Lógica para envio de vídeo
        var formData = new FormData();
        formData.append("titulo", titulo);
        formData.append("categoria", categoria);
        formData.append("fileToUpload", fileToUpload);

        $.ajax({
            type: "POST",
            url: "http://localhost:8080/videos/upload",
            data: formData,
            processData: false,
            contentType: false,
            success: function (data) {
                console.log("Vídeo enviado com sucesso:", data);
                getVideoList(currentPage, pageSize);
            },
            error: function (error) {
                console.error("Erro ao enviar vídeo:", error);
            }
        });
    });

    //Filtro de busca por titulo
    $("#buscarPorTitulo").submit(function (event) {
        event.preventDefault();
        var titulo = $("#tituloBusca").val();

        $.get(`http://localhost:8080/videos/titulo?titulo=${titulo}`, function (data) {
            displayVideoList(data)
        });
    });

    //Filtro de busca por categoria
    $("#buscarPorCategoria").submit(function (event) {
        event.preventDefault();
        var categoria = $("#categoriaBuscar").val();

        $.get(`http://localhost:8080/videos/categoria?categoria=${categoria}`, function (data) {
            displayVideoList(data)
        });
    });

    //Filtro de busca por data
    $("#buscarPorData").submit(function (event) {
        event.preventDefault();

        var dataInicio = $("#dataInicio").val();
        var dataFim = $("#dataFim").val();

        $.get(`http://localhost:8080/videos/data?dataInicio=${dataInicio}&dataFim=${dataFim}`, function (data) {
            displayVideoList(data);
        });
    });


    // Lógica para obter a lista de vídeos e exibir na página
    function getVideoList(page, size) {
        $.get(`http://localhost:8080/videos?page=${page}&size=${size}`, function (data) {
            displayVideoList(data.content)
        });
    }

    // Paginação
    var currentPage = 0;
    var pageSize = 10;

    // Evento para carregar a próxima página
    $("#loadMoreBtn").click(function () {
        currentPage++;
        getVideoList(currentPage, pageSize);
    });

    function displayVideoList(videos) {
        var videoListContainer = $("#videoList");
        videoListContainer.empty(); // Limpa o conteúdo anterior

        videos.forEach(function (video, index) {
            // Adiciona uma nova linha a cada 2 vídeos para criar 2 cards por linha
            if (index % 2 === 0) {
                $("#videoList").append("<div class='row'></div>");
            }

            // Cria o card do Bootstrap para cada vídeo
            var videoItem = $("<div class='col-md-6 mb-4'>" +
                "<div class='card'>" +
                "<video class='card-img-top' controls preload='none' width='100%' height='auto'>" +
                "<source src='http://localhost:8080/videos/" + video.id + "' type='video/mp4'>" +
                "Seu navegador não suporta o elemento <code>video</code>" +
                "</video>" +
                "<div class='card-body'>" +
                "<h5 class='card-title'>" + video.titulo + "</h5>" +
                "<p class='card-text'><b>Categoria: </b>" + video.categoria + "</p>" +
                "<p class='card-text'><b>Data de publicação: </b>" + formatarData(video.dataDeCadastro) + "</p>" +
                "<button class='btn btn-warning open-update-modal m-2'" +
                "        data-video-id='" + video.id + "'" +
                "        data-video-titulo='" + video.titulo + "'" +
                "        data-video-categoria='" + video.categoria + "'" +
                "        data-toggle='tooltip'" +
                "        data-placement='top'" +
                "        title='Atualizar Vídeo'>" +
                "    Atualizar" +
                "</button>" +
                "<button class='btn btn-danger open-delete-modal m-2'" +
                "        data-video-id='" + video.id + "'" +
                "        data-toggle='tooltip'" +
                "        data-placement='top'" +
                "        title='Excluir Vídeo'>" +
                "    Excluir" +
                "</button>" +
                "</div>" +
                "</div>" +
                "</div>");

            // Adiciona o card à última linha
            $(".row:last").append(videoItem);
        });

    }

    // Atualização de vídeo
    $("#updateForm").submit(function (event) {
        event.preventDefault();

        var videoIdToUpdate = $("#videoIdToUpdate").val();
        var novoTitulo = $("#novoTitulo").val();
        var novaCategoria = $("#novaCategoria").val();

        // Lógica para atualização de vídeo
        var formData = new FormData();
        formData.append("novoTitulo", novoTitulo);
        formData.append("novaCategoria", novaCategoria);

        $.ajax({
            type: "PUT",
            url: `http://localhost:8080/videos/${videoIdToUpdate}/update`,
            data: formData,
            processData: false,
            contentType: false,
            success: function (data) {
                console.log("Vídeo atualizado com sucesso:", data);
                $("#updateModal").modal('hide');
                getVideoList(currentPage, pageSize);
            },
            error: function (error) {
                console.error("Erro ao atualizar vídeo:", error);
            }
        });

    });

    // Evento para abrir o modal de atualização
    $(document).on("click", ".open-update-modal", function () {
        var videoIdToUpdate = $(this).data('video-id');
        var videoTitulo = $(this).data('video-titulo');
        var videoCategoria = $(this).data('video-categoria');

        // Preencher campos do modal
        $("#videoIdToUpdate").val(videoIdToUpdate);
        $("#novoTitulo").val(videoTitulo);
        $("#novaCategoria").val(videoCategoria);

        // Abrir o modal
        $("#updateModal").modal('show');
    });

    function formatarData(data) {
        var options = {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: 'numeric',
            minute: 'numeric',
            second: 'numeric',
            hour12: false
        };
        return new Intl.DateTimeFormat('pt-BR', options).format(new Date(data));
    }

    // Evento para abrir o modal de confirmação de exclusão
    $(document).on("click", ".open-delete-modal", function () {
        var videoIdToDelete = $(this).data('video-id');

        // Adiciona o ID do vídeo ao botão de confirmação de exclusão
        $("#confirmDeleteBtn").data('video-id', videoIdToDelete);

        // Abrir o modal
        $("#deleteModal").modal('show');
    });

// Evento de confirmação de exclusão
    $("#confirmDeleteBtn").click(function () {
        var videoIdToDelete = $(this).data('video-id');

        // Lógica para exclusão de vídeo
        $.ajax({
            type: "DELETE",
            url: `http://localhost:8080/videos/${videoIdToDelete}/delete`,
            success: function (data) {
                console.log("Vídeo excluído com sucesso:", data);
                $("#deleteModal").modal('hide');
                getVideoList(currentPage, pageSize);
            },
            error: function (error) {
                console.error("Erro ao excluir vídeo:", error);
            }
        });

        // Fechar o modal após a exclusão
        $("#deleteModal").modal('hide');
        getVideoList(currentPage, pageSize);
    });

    // Carregando a primeira página ao carregar a página HTML
    getVideoList(currentPage, pageSize);

});
