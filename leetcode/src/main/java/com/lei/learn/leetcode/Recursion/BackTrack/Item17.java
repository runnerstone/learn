package com.lei.learn.leetcode.Recursion.BackTrack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Item17 {
  static Map<String, String> phone = new HashMap<String, String>() {{
    put("2", "abc");
    put("3", "def");
    put("4", "ghi");
    put("5", "jkl");
    put("6", "mno");
    put("7", "pqrs");
    put("8", "tuv");
    put("9", "wxyz");
  }};

  static List<String> list = new ArrayList<>();

  public static List<String> letterCombinations(String digits) {
    if (digits.length() != 0) {
      backtrack("", digits);
    }
    return list;
  }

  public static void backtrack(String combination, String next_digits) {
    if (next_digits.length() == 0) {
      list.add(combination);
    } else {
      String number = next_digits.substring(0, 1);
      String letters = phone.get(number);
      for (int i = 0; i < letters.length(); i++) {
        String letter = letters.substring(i, i + 1);
        backtrack(combination + letter, next_digits.substring(1));
      }
    }
  }

  public static void main(String[] args) {

    String digits = "23";
    System.out.println(letterCombinations(digits).toString());
  }
}
