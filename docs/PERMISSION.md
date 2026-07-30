Được, mình bỏ hết thuật ngữ khó, đi từng mô hình bằng ví dụ đời thường trước, sau đó mới nối lại với case thực tế của
bạn.

## 1. RBAC (Role-Based Access Control) — "Phân quyền theo chức danh"

**Ý tưởng:** Không gán quyền trực tiếp cho từng người, mà gán quyền cho 1 "chức danh" (role), rồi người nào giữ chức
danh đó thì có quyền đó.

**Ví dụ đời thường:** Trong công ty bạn, "Trưởng phòng" thì được duyệt nghỉ phép, "Nhân viên" thì không. Không cần ghi
riêng "Tuấn được duyệt nghỉ phép" — chỉ cần ghi "Tuấn là Trưởng phòng", và "Trưởng phòng được duyệt nghỉ phép".

**Áp vào hệ thống của bạn:**

```
Role "ADMIN"   → được: tạo, sửa, xóa UserCatalogue
Role "EDITOR"  → được: tạo, sửa UserCatalogue (không được xóa)
Role "VIEWER"  → chỉ được: xem UserCatalogue

User "Tuấn" → gán Role "ADMIN" → tự động có mọi quyền của ADMIN
```

**Giới hạn của RBAC:** Nó chỉ trả lời được câu "role này được làm hành động X không", **không** trả lời được câu "được
làm trên record NÀO". Ví dụ: "EDITOR chỉ được sửa bài viết CỦA CHÍNH MÌNH viết, không được sửa bài của người khác" —
RBAC thuần không xử lý được câu này, vì nó không biết gì về "ai là chủ của record nào".

## 2. ABAC (Attribute-Based Access Control) — "Phân quyền theo thuộc tính, có điều kiện"

**Ý tưởng:** Thay vì chỉ hỏi "role là gì", hệ thống hỏi nhiều câu hơn dựa trên **thuộc tính** của người dùng, của
resource, và bối cảnh (thời gian, địa điểm...), rồi kết hợp lại thành 1 quyết định.

**Ví dụ đời thường:** Bảo vệ công ty cho vào cửa nếu: "là nhân viên phòng Kỹ thuật" VÀ "đang trong giờ hành chính" VÀ "
thẻ ra vào chưa hết hạn". Đây không phải 1 role đơn giản, mà là **tổ hợp nhiều điều kiện** cùng lúc.

**Áp vào hệ thống của bạn:**

```
Cho phép sửa UserCatalogue NẾU:
  user.department == catalogue.department
  VÀ user.role == "EDITOR"
  VÀ catalogue.status != "LOCKED"
```

**Khi nào cần:** Khi rule phức tạp đến mức không thể gói gọn trong "role", mà phụ thuộc vào nhiều yếu tố động (phòng
ban, trạng thái record, thời điểm...).

**Nhược điểm:** Viết nhiều rule kiểu này rải rác sẽ rất khó kiểm soát — 6 tháng sau nhìn lại không nhớ rule nào áp dụng
cho case nào, dễ chồng chéo, khó test hết các trường hợp (combinatorial explosion).

## 3. PBAC (Policy-Based Access Control) — "ABAC nhưng có tổ chức lại"

**Ý tưởng:** Giống ABAC (vẫn dựa trên điều kiện/thuộc tính), nhưng **tách phần viết rule ra khỏi code chính**, đưa vào 1
tầng riêng gọi là "policy engine" (VD: OPA - Open Policy Agent, hay thư viện Casbin). Thay vì rule nằm rải rác trong
code Java, bạn viết rule bằng 1 ngôn ngữ riêng (VD: Rego của OPA), quản lý tập trung 1 chỗ.

**Ví dụ đời thường:** Giống ABAC ở trên, nhưng thay vì mỗi phòng ban tự viết quy định riêng theo ý mình, công ty có 1
phòng Pháp chế soạn TẤT CẢ quy định thành 1 văn bản chung, ai cũng tra cứu ở đó.

**Khi nào cần:** Công ty lớn, nhiều team cùng maintain nhiều service, cần 1 nơi duy nhất định nghĩa "ai được làm gì" để
dễ audit, dễ đổi rule mà không cần deploy lại code. Với quy mô MST Software hiện tại, **cái này overkill**, chưa cần
tới.

## 4. ReBAC (Relationship-Based Access Control) — "Phân quyền theo quan hệ sở hữu"

**Ý tưởng:** Quyền không gắn với role chung chung, mà gắn với **mối quan hệ cụ thể** giữa 1 user và 1 record cụ thể.

**Ví dụ đời thường — chính là Google Docs:** Không có khái niệm "role Editor toàn hệ thống". Thay vào đó: "Tuấn là
Editor **của file A**", "Tuấn là Viewer **của file B**" — quyền khác nhau **theo từng file riêng lẻ**, không phải 1
quyền áp dụng cho mọi tài liệu.

**Áp vào hệ thống của bạn:**

```
User "Tuấn" — quan hệ "owner" — UserCatalogue #5
User "Tuấn" — quan hệ "viewer" — UserCatalogue #8
→ Tuấn sửa được #5, chỉ xem được #8, dù cùng 1 user, cùng 1 loại resource
```

**Khi nào cần:** Hệ thống có tính chất chia sẻ tài liệu/dự án theo từng cá nhân (như Notion, Google Drive, Figma) —
quyền gắn theo từng record, không theo "chức danh chung".

## 5. ACL (Access Control List) — không hẳn là 1 "mô hình", mà là **cách lưu trữ**

**Ý tưởng:** Với mỗi resource, lưu 1 danh sách "ai được làm gì trên nó". Đây là cơ chế nền để hiện thực ReBAC ở trên.

```
UserCatalogue #5:
  - Tuấn: full quyền
  - Nam: chỉ xem
```

## Bảng so sánh nhanh để bạn chọn

| Mô hình   | Câu hỏi nó trả lời                               | Độ phức tạp    | Khi nào dùng                                              |
|-----------|--------------------------------------------------|----------------|-----------------------------------------------------------|
| **RBAC**  | "Role này được làm X không?"                     | Thấp           | Mặc định, dùng cho 90% hệ thống CRUD thông thường         |
| **ABAC**  | "Với điều kiện A, B, C thì có được làm X không?" | Trung bình-cao | Khi rule phụ thuộc nhiều thuộc tính động                  |
| **PBAC**  | Giống ABAC, nhưng rule tách riêng 1 tầng         | Cao            | Doanh nghiệp lớn, nhiều team, cần audit tập trung         |
| **ReBAC** | "Quan hệ giữa user này và record này là gì?"     | Trung bình     | Hệ thống chia sẻ tài liệu, mỗi record có chủ sở hữu riêng |
| **ACL**   | Cách lưu danh sách quyền theo từng record        | —              | Cơ chế nền của ReBAC                                      |

## Case của bạn nên dùng gì?

Với `UserCatalogue`, `Product` — đây là dữ liệu **quản trị chung của công ty** (không phải kiểu "mỗi user sở hữu file
riêng như Google Docs), nên:

**Dùng RBAC làm nền** (đã hướng dẫn code ở lượt trước) là đủ cho 95% nhu cầu — ADMIN/EDITOR/VIEWER với các permission
`resource:action`.

**Chỉ thêm 1 lớp ACL/ReBAC-nhẹ** (như hàm `isOwner()` mình viết ở lượt trước) **nếu thực sự có rule** kiểu "chỉ sửa được
record do chính mình tạo" — không cần dựng hẳn 1 hệ thống ReBAC phức tạp, chỉ cần check thêm 1 điều kiện trong
`@PreAuthorize` là đủ.

**Không cần ABAC/PBAC** ở giai đoạn này — 2 mô hình này chỉ đáng đầu tư khi rule phân quyền của bạn phức tạp tới mức
RBAC + 1-2 điều kiện ownership không còn đủ diễn tả nữa.

**Câu hỏi để chốt hướng đi:** Trong hệ thống MST Software, có rule nào kiểu "user chỉ được sửa/xóa record do chính họ
tạo ra" không, hay ADMIN/EDITOR có toàn quyền trên mọi record không phân biệt ai tạo? Trả lời câu này sẽ quyết định bạn
có cần thêm lớp ownership-check hay chỉ cần RBAC thuần là đủ.