function delArticle(ArticleUrl) {
    fetch('http://localhost:7000/delarticle?url=' + ArticleUrl, {
            method: 'POST',
        }
    ).then(res => window.location.replace('http://localhost:7000/'));
}

let delButtons = document.querySelectorAll("button.ArticleDel")
Array.prototype.forEach.call(delButtons, function(button) {
    button.addEventListener('click', delArticle.bind(null, button.id));
});