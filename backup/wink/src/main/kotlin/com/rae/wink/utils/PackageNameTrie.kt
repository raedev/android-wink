package com.rae.wink.utils

/**
 * 基于Trie算法对包名进行前缀匹配。
 * 算法来自于腾讯Shadow框架。
 * @author RAE
 * @date 2022/08/16
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class PackageNameTrie {

    private class Node {
        val subNodes = mutableMapOf<String, Node>()
        var isLastPackageOfARule = false
    }

    private val root = Node()

    fun insert(packageNameRule: String) {
        var node = root
        packageNameRule.split('.').forEach {
            if (it.isEmpty()) return //"",".*",".**"这种无包名情况不允许设置

            var subNode = node.subNodes[it]
            if (subNode == null) {
                subNode = Node()
                node.subNodes[it] = subNode
            }
            node = subNode
        }
        node.isLastPackageOfARule = true
    }

    fun isMatch(packageName: String): Boolean {
        var node = root

        val split = packageName.split('.')
        val lastIndex = split.size - 1
        for ((index, name) in split.withIndex()) {
            // 只要下级包名规则中有**，就完成了匹配
            val twoStars = node.subNodes["**"]
            if (twoStars != null) {
                return true
            }

            // 剩最后一级包名时，如果规则是*则完成比配
            if (index == lastIndex) {
                val oneStar = node.subNodes["*"]
                if (oneStar != null) {
                    return true
                }
            }

            // 找不到下级包名时即匹配失败
            val subNode = node.subNodes[name]
            if (subNode == null) {
                return false
            } else {
                node = subNode
            }
        }
        return node.isLastPackageOfARule
    }


}