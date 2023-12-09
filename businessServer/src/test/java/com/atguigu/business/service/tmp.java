//import org.springframework.http.MediaType;
//        import org.springframework.web.bind.annotation.PostMapping;
//        import org.springframework.web.bind.annotation.RequestBody;
//        import org.springframework.web.bind.annotation.RequestMapping;
//        import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/search")
//public class SearchController {
//
//    private final RecommenderService recommenderService;
//    private final MovieService movieService;
//
//    public SearchController(RecommenderService recommenderService, MovieService movieService) {
//        this.recommenderService = recommenderService;
//        this.movieService = movieService;
//    }
//
//    @PostMapping(value = "/search", produces = "application/json", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public Model getSearchMovies(@RequestBody SearchRequest searchRequest, Model model) throws UnsupportedEncodingException {
//        String query = searchRequest.getQuery();
//        int num = searchRequest.getNum();
//        query = new String(query.getBytes("ISO8859-1"), "UTF-8");
//        List<Recommendation> recommendations = recommenderService.getContentBasedSearchRecommendations(new SearchRecommendationRequest(query, num));
//        model.addAttribute("success", true);
//        model.addAttribute("insurances", movieService.getRecommendeMovies(recommendations));
//        return model;
//    }
//
//    public static class SearchRequest {
//        private String query;
//        private int num;
//
//        public String getQuery() {
//            return query;
//        }
//
//        public void setQuery(String query) {
//            this.query = query;
//        }
//
//        public int getNum() {
//            return num;
//        }
//
//        public void setNum(int num) {
//            this.num = num;
//        }
//    }
//}
