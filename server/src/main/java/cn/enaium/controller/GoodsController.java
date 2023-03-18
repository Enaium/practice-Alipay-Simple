/*
 * MIT License
 *
 * Copyright (c) 2023 Enaium
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package cn.enaium.controller;

import cn.enaium.model.entity.Goods;
import cn.enaium.model.result.Result;
import cn.enaium.repository.GoodsRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author Enaium
 */
@RestController
@RequestMapping("/goods")
@AllArgsConstructor
public class GoodsController {

    private final GoodsRepository goodsRepository;

    /**
     * get all goods
     *
     * @param page current number of pages
     * @param size number of pages each page
     */
    @PostMapping("/all")
    public Result<Page<Goods>> all(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return Result.Builder.success(goodsRepository.findAll(PageRequest.of(page, size)));
    }

    /**
     * get goods info
     *
     * @param id goods id
     */
    @PostMapping("/info")
    public Result<Optional<Goods>> info(@RequestParam int id) {
        return Result.Builder.success(goodsRepository.findById(id));
    }
}
