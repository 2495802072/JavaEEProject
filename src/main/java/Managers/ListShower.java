package Managers;

import Classes.Questions;

import java.util.ArrayList;

public class ListShower {
    /**
     * 分页显示问题列表。
     *
     * @param lengthPerPage 每页显示的问题个数
     * @param page 当前页码
     * @param questionsArrayList 需要分页的问题列表
     * @return 返回当前页的问题列表
     */
    public static ArrayList<Questions> showQuestionsInPage(int lengthPerPage, int page, ArrayList<Questions> questionsArrayList) {
        // 计算从哪个问题开始
        int startIndex = (page - 1) * lengthPerPage;
        // 计算结束索引，确保不超过列表长度
        int endIndex = Math.min(startIndex + lengthPerPage, questionsArrayList.size());
        // 返回当前页的问题列表
        return new ArrayList<>(questionsArrayList.subList(startIndex, endIndex));
    }

    /**
     * 计算给定问题列表的总页数。
     *
     * @param lengthPerPage 每页显示的问题数
     * @param questionsArrayList 问题列表的数组列表
     * @return 问题的总页数
     */
    public static int GetTotalPageCount(int lengthPerPage, ArrayList<Questions> questionsArrayList) {
        // 计算总页数
        int totalPageCount = 0;
        if (questionsArrayList != null && !questionsArrayList.isEmpty()) {
            totalPageCount = (questionsArrayList.size() + lengthPerPage - 1) / lengthPerPage;
        }
        return totalPageCount;
    }
}
