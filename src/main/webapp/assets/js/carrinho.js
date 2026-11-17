(function () {
    var base = document.body.getAttribute('data-context') || '';

    function enviarDelete(url) {
        fetch(url, { method: 'DELETE' })
            .then(function (resp) {
                if (!resp.ok) {
                    throw new Error('Erro HTTP ' + resp.status);
                }
                window.location.reload();
            })
            .catch(function (erro) {
                alert('Não foi possível concluir a operação: ' + erro.message);
            });
    }

    document.querySelectorAll('.btn-remover').forEach(function (botao) {
        botao.addEventListener('click', function () {
            var id = botao.getAttribute('data-id');
            enviarDelete(base + '/carrinho/itens?id=' + encodeURIComponent(id));
        });
    });

    var limpar = document.getElementById('btn-limpar');
    if (limpar) {
        limpar.addEventListener('click', function () {
            enviarDelete(base + '/carrinho');
        });
    }
})();
