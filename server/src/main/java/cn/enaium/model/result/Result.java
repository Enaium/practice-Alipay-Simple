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

package cn.enaium.model.result;

import static cn.enaium.model.result.Result.Status.FAIL;
import static cn.enaium.model.result.Result.Status.SUCCESS;

/**
 * @author Enaium
 */
public record Result<T>(int code, String message, T metadata) {
    public enum Status {
        SUCCESS(200, "Success"),
        FAIL(999, "Fail"),
        ORDER_DOESNT_EXIST(2001, "order doesn't exist");
        public final int code;
        private final String message;

        Status(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }

    public static class Builder {
        public static <T> Result<T> success(T content) {
            return new Result<>(SUCCESS.code, SUCCESS.message, content);
        }

        public static <T> Result<T> success() {
            return new Result<>(SUCCESS.code, SUCCESS.message, null);
        }

        public static <T> Result<T> fail(String message) {
            return new Result<>(FAIL.code, message, null);
        }

        public static <T> Result<T> fail(Status status) {
            return new Result<>(status.code, status.message, null);
        }

        public static <T> Result<T> fail() {
            return new Result<>(FAIL.code, FAIL.message, null);
        }
    }
}
