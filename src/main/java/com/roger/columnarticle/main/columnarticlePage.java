package com.roger.columnarticle.main;

public class columnarticlePage {

//    /**
//     * 前往專欄文章的網頁
//     */
//    @GetMapping("/listAllColumnArticle")
//    public String listAllColumnArticle(@RequestParam(value = "page", defaultValue = "0") Integer page,
//                                       @RequestParam(value = "size", defaultValue = "10") Integer size,
//                                       ModelMap modelMap,
//                                       HttpSession session) {
//
//
//        if (session.getAttribute("loginsuccess") != null) {
//            Member member = (Member) session.getAttribute("loginsuccess");
//            List<ClickLike> clickLikeList = clickLikeService.getLikedArticlesByMember(member.getMemNo());
//            List<ArticleCollection> articleCollectionList = articleCollectionService.getArticleCollectionByMember(member.getMemNo());
//
//            // 創建一個包含點讚文章編號的列表
//            List<Integer> likedArtNoList = new ArrayList<>();
//            for (ClickLike clickLike : clickLikeList) {
//                likedArtNoList.add(clickLike.getCompositeClickLike().getArtNo());
//            }
//
//            // 創建一個包含收藏文章編號的列表
//            List<Integer> articleCollections = new ArrayList<>();
//            for (ArticleCollection articleCollection : articleCollectionList) {
//                articleCollections.add(articleCollection.getCompositeArticleCollection().getArtNo());
//            }
//
//            // 將 clickLikeList 和 likedArtNoList 存入 session
//            session.setAttribute("clickLikeList", clickLikeList);
//            session.setAttribute("likedArtNoList", likedArtNoList);
//
//            // 將 articleCollections 和 articleCollection 存入 session
//            session.setAttribute("articleCollectionList", articleCollectionList);
//            session.setAttribute("articleCollections", articleCollections);
//
//            // 一頁展示 10 篇專欄文章
//            Pageable pageable = PageRequest.of(page, size);
//            Page<ColumnArticle> columnArticlePage = columnArticleService.getAllByArtStatColumnArticles(pageable);
//            modelMap.addAttribute("columnArticlePage", columnArticlePage);
//            System.out.println("我有幾篇文章: " + columnArticlePage);
//
//            return "frontend/columnarticle/listAllColumnArticle";
//        } else {
//            // 一頁展示 10 篇專欄文章
//            Pageable pageable = PageRequest.of(page, size);
//            Page<ColumnArticle> columnArticlePage = columnArticleService.getAllByArtStatColumnArticles(pageable);
//            modelMap.addAttribute("columnArticlePage", columnArticlePage);
//            System.out.println("我有幾篇文章: " + columnArticlePage);
//
//            return "frontend/columnarticle/listAllColumnArticle";
//        }
//    }

}
