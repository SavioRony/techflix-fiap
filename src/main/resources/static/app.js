// Adicione seu código JavaScript aqui para interagir com a API

$(document).ready(function () {
    // Evento de envio do formulário de upload
    $("#uploadForm").submit(function (event) {
        event.preventDefault();

        var titulo = $("#titulo").val();
        var fileToUpload = $("#fileToUpload")[0].files[0];

        // Lógica para envio de vídeo (adapte conforme necessário)
        var formData = new FormData();
        formData.append("titulo", titulo);
        formData.append("fileToUpload", fileToUpload);

        $.ajax({
            type: "POST",
            url: "http://localhost:8080/videos/upload",
            data: formData,
            processData: false,
            contentType: false,
            success: function (data) {
                console.log("Vídeo enviado com sucesso:", data);
                // Atualize a lista de vídeos ou faça outras operações necessárias
            },
            error: function (error) {
                console.error("Erro ao enviar vídeo:", error);
            }
        });
    });

    // Lógica para obter a lista de vídeos e exibir na página (adapte conforme necessário)
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

        // Itera sobre a lista de vídeos e os exibe na página
        videos.forEach(function (video) {
            var videoItem = $("<video controls preload='none' width='720px' height='480px'>")
                .attr("src", "http://localhost:8080/videos/" + video.id)
                .append("<source src='http://localhost:8080/videos/" + video.id + "' type='video/mp4'>")
                .append("Seu navegador não suporta o elemento <code>video</code>.");

            videoListContainer.append(videoItem);
        });
    }

    // Carregando a primeira página ao carregar a página HTML
    getVideoList(currentPage, pageSize);

});
