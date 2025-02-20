package cn.anlucky.luckyadmin.gen;

public class MysqlGen {

    public static void main(String[] args) {
        // 代码生成需要使用到全局的SpringBoot的注入对象，因此直接在Main 方法中调用是无法取得的
        // 所以需要自己手动编写SpringBoot Test测试类方法

        /*
             @Autowired
             private GenerationService generationService;

             @Test
             void previewCode() {
                 Map<String, String> map = generationService.previewCode("cn.anlucky.luckyadmin", "system", "sys_users");
                 String s = map.get(GenerationPo.VUE);
                 System.out.println("s = " + s);
             }

             @Test
             void generateCode() {
                generationService.generateCode("cn.anlucky.luckyadmin", "system", "sys_users");
             }
         */
    }


}
