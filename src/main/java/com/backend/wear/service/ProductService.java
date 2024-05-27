package com.backend.wear.service;

import com.backend.wear.dto.*;
import com.backend.wear.dto.product.ProductPostRequestDto;
import com.backend.wear.dto.product.ProductRequestDto;
import com.backend.wear.dto.product.ProductResponseDto;
import com.backend.wear.dto.product.SearchResponseDto;
import com.backend.wear.dto.user.UserResponseDto;
import com.backend.wear.entity.*;
import com.backend.wear.repository.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductService {

    private static final int PAGE_SIZE=12;
    private static final int SEARCH_SIZE=10;

    private final ProductRepository productRepository;
    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private final BlockedUserRepository blockedUserRepository;

    private final RedisTemplate<String, String> searchLogRedisTemplate;
    private ZSetOperations<String, String> zSetOperations;

    // ObjectMapper 생성
    ObjectMapper objectMapper = new ObjectMapper();

    /* // JSON 배열 파싱
     String[] array = objectMapper.readValue(jsonString, String[].class);
 */
    // List<String>를 JSON 문자열로 변환하는 메서드
    private String convertImageListToJson(List<String> imageList) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(imageList);
    }

//    // JSON 문자열을 List<String>으로 변환하는 메서드
//    private List<String> convertJsonToImageList(String json) throws JsonProcessingException {
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper.readValue(json, new TypeReference<List<String>>() {});
//    }

    // JSON 문자열을 String[]으로 변환하는 메서드
    private  String[] convertImageJsonToArray(String productImageJson) {
        try {
            return objectMapper.readValue(productImageJson, String[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }

    @Autowired
    public ProductService(ProductRepository productRepository, WishRepository wishRepository, UserRepository userRepository,
                          CategoryRepository categoryRepository, BlockedUserRepository blockedUserRepository,
                          RedisTemplate<String, String> searchLogRedisTemplate){
        this.productRepository=productRepository;
        this.wishRepository=wishRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.blockedUserRepository=blockedUserRepository;
        this.searchLogRedisTemplate=searchLogRedisTemplate;
    }

    // 카테고리별, 최신순 페이지네이션
    @Transactional
    public Page<ProductResponseDto.ScreenDto> findProductsByCategory(String categoryName, Long userId, Integer pageNumber)
            throws Exception{

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        Page<Product> productsPage;

        // 대학 아이디 pk
        Long userUniversityId = user.getUniversity().getId();

        // 내간 차단한 유저 아이디 리스트
        List<Long> blockedUserIdList = blockedUserRepository.findByUserId(userId);

        // 나를 차단한 유저 아이디 리스트
        List<Long> userIdListBlocked = blockedUserRepository.findByUserIdBlocked(userId);
//        for(int i=0;i<blockedUserIdList.size();i++)
//            log.info("차단 유저 아이디: "+blockedUserIdList.get(i));

        if(categoryName.equals("전체"))
            productsPage = productRepository.findAllProductPage(userUniversityId, blockedUserIdList, userIdListBlocked, pageRequest(pageNumber));

        else
            productsPage = productRepository.findByCategoryNamePage(categoryName,userUniversityId,blockedUserIdList, userIdListBlocked, pageRequest(pageNumber));

        // 카테고리별 상품이 없는 경우
        if(productsPage.isEmpty())
            throw new IllegalArgumentException("카테고리와 일치하는 판매중인 상품이 없습니다.");

        return productsPage.map(product -> mapToScreenDto(product, userId));
    }

    // 카테고리별, 판매중, 최신순 페이지네이션
    @Transactional
    public Page<ProductResponseDto.ScreenDto> findProductsByCategoryOnSale(String categoryName, Long userId, Integer pageNumber )
            throws Exception{
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        Page<Product> productsPage;
        Long userUniversityId = user.getUniversity().getId();
        List<Long> blockedUserIdList = blockedUserRepository.findByUserId(userId);
        List<Long> userIdListBlocked = blockedUserRepository.findByUserIdBlocked(userId);

        // 전체, 판매중, 최신순
        if(categoryName.equals("전체")){
            productsPage=productRepository
                    .findByPostStatusPage(userUniversityId, blockedUserIdList, userIdListBlocked, pageRequest(pageNumber));
        }

        //카테고리별 판매중 최신순
        else{
            productsPage =productRepository
                    .findByCategoryNameAndPostStatusPage(categoryName,userUniversityId, blockedUserIdList, userIdListBlocked, pageRequest(pageNumber));
        }

        // 카테고리별 상품이 없는 경우
        if(productsPage.isEmpty())
            throw new IllegalArgumentException("카테고리와 일치하는 판매중인 상품이 없습니다.");

        return productsPage.map(product -> mapToScreenDto(product, userId));
    }

    //상품 검색하기(productName 검색, 카테고리 검색)
    // 차단 유저 상품 보이지 않도록
    // 같은 대학 상품만 보이도록 페이지네이션 적용
    @Transactional
    public Page <ProductResponseDto.ScreenDto> searchProductByProductNameAndCategory(String searchName, String categoryName,
                                                                                     Long userId, Integer pageNumber) throws Exception{
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        Long userUniversityId = user.getUniversity().getId();
        // 내간 차단한 유저 아이디 리스트
        List<Long> blockedUserIdList = blockedUserRepository.findByUserId(userId);
        // 나를 차단한 유저 아이디 리스트
        List<Long> userIdListBlocked = blockedUserRepository.findByUserIdBlocked(userId);

        Page <Product>  filteredProductsPage;

        if(categoryName.equals("전체")) // 검색어와 일치하는 전체 상품 조회
            filteredProductsPage = productRepository.findByProductName(searchName, userUniversityId,
                    blockedUserIdList, userIdListBlocked,pageRequest(pageNumber));
        else // 검색어, 카테고리 일치
            filteredProductsPage  = productRepository.findByProductNameAndCategoryName(searchName, categoryName, userUniversityId,
                    blockedUserIdList,userIdListBlocked,pageRequest(pageNumber));

        // 검색어와 일치하는 상품이 없는 경우
        if(filteredProductsPage.isEmpty())
            throw new IllegalArgumentException("검색어, 카테고리와 일치하는 상품이 없습니다.");

        return filteredProductsPage.map(product -> mapToScreenDto(product, userId));
    }

    // 상품 검색
    // 카테고리별, 판매 중인 상품만 보기
    @Transactional
    public Page <ProductResponseDto.ScreenDto> searchProductByProductNameAndCategoryOnSale(String searchName, String categoryName,
                                                                                           Long userId, Integer pageNumber) throws Exception{
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        Long userUniversityId = user.getUniversity().getId();
        List<Long> blockedUserIdList = blockedUserRepository.findByUserId(userId);
        List<Long> userIdListBlocked = blockedUserRepository.findByUserIdBlocked(userId);

        Page <Product>  filteredProductsPage;

        if(categoryName.equals("전체")) // 검색어와 일치하는 전체, 판매 중 상품 조회
            filteredProductsPage = productRepository.findByProductNameOnSale(searchName, userUniversityId,
                    blockedUserIdList, userIdListBlocked, pageRequest(pageNumber));
        else // 검색어, 카테고리와 일치하는 판매 중 상품 조회
            filteredProductsPage  = productRepository.findByProductNameAndCategoryNameOnSale(searchName,categoryName,userUniversityId,
                    blockedUserIdList,userIdListBlocked,pageRequest((pageNumber)));
        // 검색어와 일치하는 상품이 없는 경우
        if(filteredProductsPage.isEmpty())
            throw new IllegalArgumentException("검색어, 카테고리와 일치하는 판매중인 상품이 없습니다.");

        return filteredProductsPage.map(product -> mapToScreenDto(product, userId));
    }

    // 카테고리 검색 상품 dto 매핑
    private ProductResponseDto.ScreenDto mapToScreenDto(Product product, Long userId){

        // JSON 배열 파싱
        String[] productImageArray = convertImageJsonToArray(product.getProductImage());

        // 사용자의 상품 찜 여부 확인
        boolean isSelected = wishRepository.findByUserIdAndProductId(userId, product.getId()).isPresent();

        // DTO 생성
        return ProductResponseDto.ScreenDto.builder()
                .id(product.getId())
                .price(product.getPrice())
                .productName(product.getProductName())
                .productStatus(product.getProductStatus())
                .postStatus(product.getPostStatus())
                .productImage(productImageArray)
                .isSelected(isSelected)
                .time(ConvertTime.convertLocalDatetimeToTime(product.getCreatedAt()))
                .build();
    }

    // 상품 12개씩 최신순으로 정렬
    private Pageable pageRequest(Integer pageNumber){
        return
                PageRequest.of(pageNumber, PAGE_SIZE, Sort.by("createdAt").descending());
    }

    // 상품 상세 조회
    @Transactional
    public ProductResponseDto.DetailDto getProductPost(Long userId, Long productId) throws Exception {
        Product product  = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "상품을 찾지 못하였습니다."));

        // 판매자
        User user=product.getUser();

        // JSON 배열 파싱
        // 판매자 프로필 이미지 배열로 변환
        String[] profileImageArray = convertImageJsonToArray(user.getProfileImage());

        // 상품 이미지 배열로 변환
        // JSON 배열 파싱
        String[] productImageArray = convertImageJsonToArray(product.getProductImage());

        UserResponseDto.SellerDto seller =  UserResponseDto.SellerDto.builder()
                .id(user.getId())
                .nickName(user.getNickName())
                .profileImage(profileImageArray)
                .level(user.getLevel().getLabel())
                .build();

        // 사용자의 상품 찜 여부 확인
        boolean isSelected = wishRepository.findByUserIdAndProductId(userId, product.getId()).isPresent();

        return ProductResponseDto.DetailDto.builder()
                .id(product.getId())
                .seller(seller)
                .price(product.getPrice())
                .productName(product.getProductName())
                .productStatus(product.getProductStatus())
                .postStatus(product.getPostStatus())
                .productContent(product.getProductContent())
                .productImage(productImageArray)
                .place(product.getPlace())
                .createdTime(product.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")))
                .time(ConvertTime.convertLocalDatetimeToTime(product.getCreatedAt()))
                .isSelected(isSelected)
                .isPrivate(product.isPrivate())
                .build();
    }

    //상품 등록하기
    @Transactional
    public void createProductPost(ProductPostRequestDto requestDTO, Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        Category category = categoryRepository.findByCategoryName(requestDTO.getCategoryName());

        if (category == null) {
            throw new IllegalArgumentException("해당 카테고리를 찾지 못하였습니다.");
        }

        // List<String>을 JSON 문자열로 변환
        String productImageJson = convertImageListToJson(requestDTO.getProductImage());

        // 제공된 Product 객체의 데이터를 사용하여 새로운 Product 객체 생성
        Product newProduct = Product.builder()
                .productName(requestDTO.getProductName())
                .price(requestDTO.getPrice())
                .productImage(productImageJson) // JSON 문자열로 저장
                .productContent(requestDTO.getProductContent())
                .productStatus(requestDTO.getProductStatus())
                .place(requestDTO.getPlace())
                .user(user)
                .category(category)
                .build();

        productRepository.save(newProduct);
    }

    // 상품 수정을 위한 정보
    @Transactional
    public ProductResponseDto.EditDto getProductPostToUpdate(Long userId, Long productId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(" 찾지 못하였습니다."));

        // JSON 배열 파싱
        String[] productImageArray = convertImageJsonToArray(product.getProductImage());

        return ProductResponseDto.EditDto.builder()
                .id(product.getId())
                .productImage(productImageArray )
                .productName(product.getProductName())
                .categoryName(product.getCategory().getCategoryName())
                .productStatus(product.getProductStatus())
                .productContent(product.getProductContent())
                .price(product.getPrice())
                .place(product.getPlace())
                .postStatus(product.getPostStatus())
                .build();
    }

    // 상품 정보 변경(전체를 받아서 전체를 변경)
    @Transactional
    public void updateProductPost(ProductPostRequestDto requestDTO, Long userId, Long productId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        // 기존 제품을 찾아 업데이트
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("제품을 찾지 못하였습니다."));

        Category category = categoryRepository.findByCategoryName(requestDTO.getCategoryName());

        if (category == null) {
            throw new IllegalArgumentException("해당 카테고리를 찾지 못하였습니다.");
        }

        // List<String>을 JSON 문자열로 변환
        String productImageJson = convertImageListToJson(requestDTO.getProductImage());

        // 업데이트된 정보로 기존 제품 업데이트
        product.setProductName(requestDTO.getProductName());
        product.setPrice(requestDTO.getPrice());
        product.setProductImage(productImageJson);
        product.setProductContent(requestDTO.getProductContent());
        product.setProductStatus(requestDTO.getProductStatus());
        product.setPlace(requestDTO.getPlace());
        product.setUser(user);
        product.setCategory(category);

        // updatedAt 업데이트
        product.setUpdatedAt(product.getUpdatedAt());

        // productRepository.save(product);
    }

    // 상품 판매 상태 변경 (판매 완료 또는 판매 중)
    @Transactional
    public void updateProductPostStatus(ProductRequestDto requestDto , Long userId ) throws Exception {

        System.out.println(requestDto);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        if(user != null){
            //해당 사용자의 상품을 제대로 요청했는지 확인
            Product product = productRepository.findById(requestDto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾지 못하였습니다."));

            System.out.println(requestDto.getPostStatus());


            // 요청으로 받은 postStatus를 상품의 상태로 설정합니다.
            product.setPostStatus(requestDto.getPostStatus());

            // updatedAt 업데이트
            product.setUpdatedAt(product.getUpdatedAt());

            // 변경된 상품 정보를 저장합니다.
            //    productRepository.save(product);
        }
    }

    // 상품 숨기기 || 숨김 해제하기
    @Transactional
    public void updateProductPostPrivate( Long userId, Long productId ) throws Exception {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        if(user != null){
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾지 못하였습니다."));

            if(product.isPrivate() == true){
                product.setPrivate(false);
            }else{
                product.setPrivate(true);
            }
            // updatedAt 업데이트
            product.setUpdatedAt(product.getUpdatedAt());
            //productRepository.save(product);
        }
    }

    // 상품 삭제
    @Transactional
    public void deleteProductPost( Long userId, Long productId ) throws Exception {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        if(user != null) {
            //해당 사용자의 상품을 제대로 요청했는지 확인
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾지 못하였습니다."));

            productRepository.deleteByUserAndProduct(userId,productId);
        }
    }

    // 상품 찜하기
    @Transactional
    public void selectProduct(Long userId, Long productId) throws Exception{
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾지 못하였습니다."));

        Wish wish = Wish.builder()
                .user(user)
                .product(product)
                .build();

        // 찜
        wishRepository.save(wish);
    }

    // 상품 찜 해제
    @Transactional
    public void deselectProduct(Long userId, Long productId) throws Exception{
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾지 못하였습니다."));

        Wish wish = wishRepository.findByUserIdAndProductId(userId,productId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 아이디와 상품 아이디가 일치하는 찜한 상품 없음"));

        // 찜 해제
        wishRepository.delete(wish);
    }

    // 사용자 차단하기
    @Transactional
    public void blockedUser(Long userId, Long blockedUserId) throws Exception{
        // 로그인한 사용자
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인한 사용자를 찾지 못하였습니다."));

        // 차단하고 싶은 사용자
        User blockedUser = userRepository.findById(blockedUserId)
                .orElseThrow(() -> new IllegalArgumentException("차단하려고 하는 사용자를 찾지 못하였습니다."));

        BlockedUser block = BlockedUser.builder()
                .user(user)
                .blockedUserId(blockedUserId)
                .build();

        // 사용자 차단
        blockedUserRepository.save(block);
    }

    // 최근 검색어 저장
    public void saveRecentSearchLog(Long userId, String searchName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인한 사용자를 찾지 못하였습니다."));

        if(searchName == null || searchName.isEmpty())
            throw new IllegalArgumentException("빈 검색어 입력 불가");

        String key = "SearchLog" + userId;
        log.info("입력한 검색어 key, value: " + key + ", " + searchName);

        // 이미 존재하는지 확인
        List<String> searchList = searchLogRedisTemplate.opsForList().range(key, 0, -1);
        if (searchList != null && searchList.contains(searchName)) {
            log.info("exists: true");
            // 이미 존재하는 경우, 해당 검색어를 리스트의 맨 앞으로 이동시켜야 하기 때문에 제거
            searchLogRedisTemplate.opsForList().remove(key, 0, searchName);
        }

        Long size = searchLogRedisTemplate.opsForList().size(key);

        // redis의 현재 크기가 10인 경우
        if (size != null && size == SEARCH_SIZE) {
            // rightPop을 통해 가장 오래된 데이터 삭제
            searchLogRedisTemplate.opsForList().rightPop(key);
        }

        // 새로운 검색어를 추가
        searchLogRedisTemplate.opsForList().leftPush(key, searchName);

        // 전체 사용자 대상 검색어 추가
        Double score = searchLogRedisTemplate.opsForZSet().score("SearchLog", searchName);
        searchLogRedisTemplate.opsForZSet().add("SearchLog", searchName, (score != null) ? score + 1 : 1);
    }

    // 최근 검색 기록 조회
    public SearchResponseDto.UserDto findRecentSearchLogs(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인한 사용자를 찾지 못하였습니다."));

        return SearchResponseDto.UserDto.builder()
                .searchNameList( searchLogRedisTemplate.opsForList().range("SearchLog"+userId, 0, 10))
                .build();
    }

    // 검색어와 일치하는 사용자의 최근 검색어 삭제
    public Long deleteRecentSearchLog(Long userId, String searchName) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그인한 사용자를 찾지 못하였습니다."));

        String key = "SearchLog" + userId;

        Long count = searchLogRedisTemplate.opsForList().remove(key, 1, searchName);

        return count;
    }

    @Transactional
    // 사용자 최근 검색어 전체 삭제
    public void deleteAllSearchNameByUser(Long userId){
        searchLogRedisTemplate.keys("SearchLog"+userId).stream().forEach(k-> {
            searchLogRedisTemplate.delete(k);
        });
    }

    // 인기 검색어 스케줄링
    @Transactional
    public SearchResponseDto.RankDto getSearchNameRank(){
        LocalDateTime now = LocalDateTime.now();
        String date = ConvertTime.convertLocalDateTimeToDate(now); //날짜
        String time = ConvertTime.convertLocalDateTimeToTime(now); //시간

        log.info("인기 검색어 스케줄링 실행");

        return SearchResponseDto.RankDto.builder()
                .searchNameRankList(searchNameRankList())
                .date(date)
                .time(time)
                .build();
    }

    // 인기 검색어 조회
    private List<String> searchNameRankList(){
        String key= "SearchLog";
        ZSetOperations<String, String> zSetOperations = searchLogRedisTemplate.opsForZSet();
        // score 순으로 10개 보여줌 (value, score) 형태
        Set<ZSetOperations.TypedTuple<String>> typedTuples = zSetOperations.reverseRangeWithScores(key, 0, 9);

        // 검색어만 추출하여 리스트로 변환
        if (typedTuples != null) {
            return typedTuples.stream()
                    .map(ZSetOperations.TypedTuple::getValue)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}