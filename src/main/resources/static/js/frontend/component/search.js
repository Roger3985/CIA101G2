document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('searchInput');
    const searchResults = document.getElementById('searchResults');
    let currentQuery = '';

    searchInput.addEventListener('input', function() {
        const keyword = this.value;
        currentQuery = keyword;
        if (keyword.length >= 1) {
            axios.get('/frontend/search', {
                params: {
                    query: keyword,
                    page: 0,
                    size: 5
                }
            })
                .then(function(response) {
                    const combinedResults = response.data.results;
                    const hasMoreResults = response.data.hasMoreResults;

                    let productResults = [];
                    let rentalResults = [];

                    combinedResults.forEach(result => {
                        if (result.productName) {
                            productResults.push(result);
                        } else if (result.rentalName) {
                            rentalResults.push(result);
                        }
                    });

                    let resultsHtml = '';

                    if (productResults.length > 0) {
                        resultsHtml += '<h3>商品</h3>';
                        resultsHtml += productResults.map(result => `
                        <a href="/frontend/product/oneProduct?productNo=${result.productNo}" class="d-block mb-2">
                            <h5>${'商品類別編號: ' + result.productCategory.productCatNo + ' , 商品類別名稱: ' + result.productCategory.productCatName}</h5>
                            <p>${'商品名稱: ' + result.productName + ' , 商品資訊: '  + result.productInfo}</p>
                        </a>
                    `).join('');
                    }

                    if (rentalResults.length > 0) {
                        resultsHtml += '<h3>租借品</h3>';
                        resultsHtml += rentalResults.map(result => `
                        <a href="/frontend/rental/Rental?rentalNo=${result.rentalNo}" class="d-block mb-2">
                            <h4>${'租借品標號: ' + result.rentalCategory.rentalCatNo + ' , 租借品類別名稱: ' + result.rentalCategory.rentalCatName}</h4>
                            <p>${'租借品名稱: ' + result.rentalName + ' ,  租借品資訊: ' + result.rentalInfo}</p>
                        </a>
                    `).join('');
                    }

                    searchResults.innerHTML = resultsHtml;

                    // 根據後端判斷搜尋結果超過5筆的話返回hasMoreResults做標誌來顯示
                    if (hasMoreResults) {
                        const moreResultsHtml = `
                            <div id="moreResults" class="text-center mt-3">
                                <button type="button" class="btn btn-outline-primary" id="moreResultsButton">更多搜尋結果</button>
                            </div>
                        `;
                        searchResults.innerHTML += moreResultsHtml;
                    }
                })
                .catch(function(error) {
                    searchResults.innerHTML = '<p>發生錯誤</p>';
                    console.error('搜索錯誤:', error);
                });
        } else {
            searchResults.innerHTML = '';
            const moreResultsElement = document.getElementById('moreResults');
            if (moreResultsElement) {
                moreResultsElement.style.display = 'none'; // 隱藏“更多搜索結果”按鈕
            }
        }
    });

    searchResults.addEventListener('click', function(event) {
        if (event.target && event.target.id === 'moreResultsButton') {
            const form = document.createElement('form');
            form.method = 'GET';
            form.action = '/frontend/searchMore'; // 修改為get請求

            const queryInput = document.createElement('input');
            queryInput.type = 'hidden';
            queryInput.name = 'searchQuery'; // input元素的名稱
            queryInput.value = currentQuery; // keyword

            form.appendChild(queryInput);

            document.body.appendChild(form);
            form.submit();
        }
    });
});
