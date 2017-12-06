package com.sinosoft.fhcs.android.util;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.entity.Department;
import com.sinosoft.fhcs.android.entity.DoctorBean;

import java.util.ArrayList;
import java.util.List;

public class TestData {

    private static Department department;

    public static Department departmentData(int de){
        switch (de) {
            case Constant.department_erke://儿科
                department = new Department();
                department.setName("儿科");
                department.setDrawable(R.drawable.icon_department_pediatrics);
                department.setAbstracts("新生儿科拥有一支高素质的医护队伍，九名医生中正高职2人，副高职3人，中级4人，其中博士3人，博士后2人，硕士2人。多人曾赴国内外学习。护理队伍拥有丰富的护理经验，本科毕业护士15名、大专10名，中专5人，副主任护师1名、主管护师5人，护师10名。已成功开展外周动、静脉同步自动换血术、腋静脉穿刺，动、静脉留置技术。在全省最早开展了新生儿抚触及游泳，取得了非常显著的效果。");
                break;
            case Constant.department_fuke://妇科
                department = new Department();
                department.setName("妇科");
                department.setDrawable(R.drawable.icon_department_gynecology);
                department.setAbstracts("北大妇产儿童医院妇科是国家重点学科之一，是硕士博士培养单位及博士后流动点，具有先进的诊疗设备和高水平的医护人员，对于常见妇科疾病包括子宫肌瘤、子宫内膜异位症、妇科内分泌疾病、盆腔炎症、生殖道感染性疾病、女性尿失禁，及常见妇科肿瘤如卵巢癌、宫颈癌子宫内膜癌等的研究均有着自己的特色，达到国内国际先进水平。");
                break;
            case Constant.department_zhongyike://中医科
                department = new Department();
                department.setName("中医科");
                department.setDrawable(R.drawable.icon_department_medicine);
                department.setAbstracts("中医科采用中药治疗各种冠心病、心律失常、脑梗塞、脑动脉硬化、顽固性头痛、急慢性肾炎、泌尿系结石、男科病、脾胃病、糖尿病以及心身疾病。运用针灸、理疗等方法治疗中风、偏瘫、风湿性关节炎、哮喘等疾病，开设针灸减肥门诊，为广大肥胖患者减轻痛苦。微电脑牵引仪治疗颈椎病、腰椎增生及腰椎间盘突出症，效果显著。");
                break;
            case Constant.department_pifuke://皮肤科
                department = new Department();
                department.setName("皮肤科");
                department.setDrawable(R.drawable.icon_department_dermatologist);
                department.setAbstracts("皮肤科属于外科，主要治疗各种皮肤病，常见皮肤病有牛皮癣 、 疱疹 、酒渣鼻 、脓疱疮 、化脓菌感染 、疤痕 、癣 、鱼鳞病 、腋臭 、青春痘 、毛囊炎 、斑秃脱发 、男科炎症 、婴儿尿布疹 、鸡眼 、雀斑 、汗疱疹 、螨虫性皮炎 、白癜风 、湿疹 、灰指甲 、硬皮病 、皮肤瘙痒 、口腔部护理 、脱毛 、黄褐斑等。");
                break;
            case Constant.department_xinlike://心理科
                department = new Department();
                department.setName("心理科");
                department.setDrawable(R.drawable.icon_department_psychological_clinic);
                department.setAbstracts("临床心理科是为社会人群提供心理卫生服务的特色专科，开设有心理测验、心理咨询与治疗、生物反馈治疗、脑功能检查、经颅磁刺激治疗等专业。拥有目前国内最先进的心理CT系统、脑涨落图检查仪、经颅磁刺激治疗仪、精神压力分析仪、多参数多媒体生物反馈系统、微电流脑导入治疗系统等，负责为人群提供个性、智能和心理健康测评、健康指导、心理治疗等服务，对各类心理冲突、人际关系问题、创伤后应激障碍、家庭问题、婚姻问题、学习问题、睡眠障碍以及各类心理疾病患者提供服务，为人群提供脑健康检查和咨询，并开展各类专业性的心理治疗业务，如：认知行为治疗、艺术治疗等。");
                break;
            case Constant.department_xiaohuake://消化科
                department = new Department();
                department.setName("消化科");
                department.setDrawable(R.drawable.icon_department_gastroenterology_dept);
                department.setAbstracts("消化科包括消化内科和消化外科，是各级医院为了诊疗消化系统疾病而设置的临床科室。治疗的疾病包括食管、胃、肠、肝、胆、胰以及腹膜、肠系膜、网膜等脏器的疾病。");
                break;
            case Constant.department_puwaike://普外科
                department = new Department();
                department.setName("普外科");
                department.setDrawable(R.drawable.icon_department_general_surgery_dept);
                department.setAbstracts("普外科即普通外科，一般综合性医院外科除普外科外还有骨科、神经外科、心胸外科、泌尿外科等。有的医院甚至将普外科更细的分为颈乳科、胃肠外科、肝胆胰脾外科等，还有肛肠科、烧伤整形科、血管外科、小儿外科、移植外科、营养科等都与普外科有关系。");
                break;
            default:
                break;
        }
        return department;
    }

    public static List<DoctorBean> doctorData(int de){
        List<DoctorBean> docList = null;
        switch (de) {
            case Constant.department_erke://儿科
                docList =  getErKe();
                break;
            case Constant.department_fuke://妇科
                docList =  FuKe();
                break;
            case Constant.department_zhongyike://中医科
                docList = ZhongYiKe();
                break;
            case Constant.department_pifuke://皮肤科
                docList = PiFuKe();
                break;
            case Constant.department_xinlike://心理科
                docList = XinLiKe();
                break;
            case Constant.department_xiaohuake://消化科
                docList = XiaoHuaKe();
                break;
            case Constant.department_puwaike://普外科
                docList = PuWaiKe();
                break;
            default:
                break;
        }
        return docList;
    }

    //儿科
    private static List<DoctorBean> getErKe() {
        // TODO Auto-generated method stub
        List<DoctorBean> docList = new ArrayList<DoctorBean>();
        DoctorBean doctorBean0 = new DoctorBean();
        doctorBean0.setdId(0);
        doctorBean0.setDoctorName("李亮亮");
        doctorBean0.setDoctorJob("主治医师");
        doctorBean0.setDoctorGood("擅长小儿呼吸、消化、泌尿等常见病的诊治以及新生儿、小儿急重症的抢救和治疗。");
        doctorBean0.setDoctorAbstrack("儿科主治医师，中国平安金牌专家。国家执业医师。5年内科工作经验，曾任职广州市妇女儿童医疗中心儿科（曾在广州市妇女儿童医疗中心急救转运中心工作一年，参与广州市周边急重症儿童转运救治，行程超过10万公里，转运救治上千名儿童）。毕业于中山大学儿科。");

        DoctorBean doctorBean1 = new DoctorBean();
        doctorBean1.setdId(1);
        doctorBean1.setDoctorName("田金娜");
        doctorBean1.setDoctorJob("主治医师");
        doctorBean1.setDoctorGood("小儿反复呼吸道感染、急慢性咳嗽、鼻炎、肺炎、哮喘等呼吸道疾病以及多汗、厌食、便秘、腹泻、紫癜等儿科杂病。");
        doctorBean1.setDoctorAbstrack("田金娜，女，博士，副主任医师，副教授，全国中医药高等教育学会儿科教育研究会理事，四川省中医药学会儿科专业委员会委员兼秘书。毕业于成都中医药大学，获中医儿科学硕/博士学位，主要从事中医儿科学的临床、教学及科研工作，研究方向为小儿呼吸及消化系统疾病，擅长治疗小儿反复呼吸道感染、急慢性咳嗽、鼻炎、肺炎、哮喘等呼吸道疾病以及多汗、厌食、便秘、腹泻、紫癜等儿科杂病。");

        DoctorBean doctorBean2 = new DoctorBean();
        doctorBean2.setdId(2);
        doctorBean2.setDoctorName("解海英");
        doctorBean2.setDoctorJob("住院医师");
        doctorBean2.setDoctorGood("哮喘、呼吸道感染、发热、腹泻、厌食、过敏性鼻炎、皮疹、多动症、抽动症等疾病擅长小儿呼吸、消化、泌尿等常见病的诊治以及新生儿、小儿急重症的抢救和治疗。");
        doctorBean2.setDoctorAbstrack("教授，博士生导师。成都中医药大学毕业，硕士。中国中医药高等教育学会儿科分会常务副理事长、四川省中医学会儿科专业委员会副主任委员、四川省中西医结合学会儿科专业委员会副主任委员、呼吸专业委员会委员、四川省儿童哮喘防治协作组副组长，成都医学会儿科专业委员会委员。擅长治疗：哮喘、呼吸道感染、发热、腹泻、厌食、过敏性鼻炎、皮疹、多动症、抽动症等疾病。");

        DoctorBean doctorBean3 = new DoctorBean();
        doctorBean3.setdId(4);
        doctorBean3.setDoctorName("王毅");
        doctorBean3.setDoctorJob("主治医师");
        doctorBean3.setDoctorGood("治疗儿童哮喘、咳嗽以及反复呼吸道感染，婴幼儿腹泻、便秘、厌食及多汗症");
        doctorBean3.setDoctorAbstrack("儿科主治医师，中国平安金牌专家。国家执业医师。5年内科工作经验，曾任职广州市妇女儿童医疗中心儿科（曾在广州市妇女儿童医疗中心急救转运中心工作一年，参与广州市周边急重症儿童转运救治，行程超过10万公里，转运救治上千名儿童）。毕业于中山大学儿科。");

        DoctorBean doctorBean4 = new DoctorBean();
        doctorBean4.setdId(5);
        doctorBean4.setDoctorName("高铭");
        doctorBean4.setDoctorJob("主治医师");
        doctorBean4.setDoctorGood("血液系统方面如血小板减少性紫癜，过敏性紫癜；泌尿系统方面如肾炎，肾病及一些疑难杂病的治疗等  ");
        doctorBean4.setDoctorAbstrack("教授，成都中医大学“十大首届名师”称号,并兼职四川省专家评审库成员，四川省中医儿科学会委员，成都市内妇儿科学会副主任委员和成都中医药大学教学督导组成员。四川省名中医。 ");


        DoctorBean doctorBean5 = new DoctorBean();
        doctorBean5.setdId(5);
        doctorBean5.setDoctorName("郭爱萍");
        doctorBean5.setDoctorJob("主治医师");
        doctorBean5.setDoctorGood("擅长小儿呼吸、消化、泌尿等常见病的诊治以及新生儿、小儿急重症的抢救和治疗。");
        doctorBean5.setDoctorAbstrack("教授，科主任，博士生导师。四川省名中医，首批国家中医药管理局全国优秀中医临床人才，四川省中医药科学与技术带头人，现任四川省中医儿科专委会委员，四川省中西结合肾病专委会委员，中华医学会四川省及成都市儿肾专委会委员，成都市中西结合儿科专委会委员。");

        docList.add(doctorBean0);
        docList.add(doctorBean1);
        docList.add(doctorBean2);
        docList.add(doctorBean3);
        docList.add(doctorBean4);
        docList.add(doctorBean5);

        return docList;
    }
    //妇科
    private static List<DoctorBean> FuKe() {
        // TODO Auto-generated method stub
        List<DoctorBean> docList = new ArrayList<DoctorBean>();
        DoctorBean doctorBean0 = new DoctorBean();
        doctorBean0.setdId(0);
        doctorBean0.setDoctorName("彭卫东");
        doctorBean0.setDoctorJob("  主任医师");
        doctorBean0.setDoctorGood("妇科肿瘤、盆腔炎性疾病、月经不调、痛经、围绝经期综合征等妇科常见疾病和妇科疑难杂疾病的中医和中西结合治疗；各种妇科腹腔镜手术、腹式及阴式子宫切除术等妇科疑难复杂手术及围手术期的中医药调治");
        doctorBean0.setDoctorAbstrack("教授，成都中医药大学毕业，硕士。擅长妇科肿瘤、盆腔炎性疾病、月经不调、痛经、围绝经期综合征等妇科常见疾病和妇科疑难杂疾病的中医和中西结合治疗；擅长各种妇科腹腔镜手术、腹式及阴式子宫切除术等妇科疑难复杂手术及围手术期的中医药调治。全国第二批老中医药专家学术经验继承人，世界中医药学会妇科专业委员会委员。");

        DoctorBean doctorBean1 = new DoctorBean();
        doctorBean1.setdId(1);
        doctorBean1.setDoctorName("杨家林");
        doctorBean1.setDoctorJob("主治医师");
        doctorBean1.setDoctorGood("月经不调、痛经、慢性盆腔炎、更年期综合征、子宫肌瘤、绝经后骨质疏松症、先兆流产、妊娠剧吐、人流术后并发症、外阴白色病损等疾病的中医治疗");
        doctorBean1.setDoctorAbstrack("女，主任医师，教授，博士生导师，享受国务院政府特殊津贴专家、卫生部、国家中医药管理局确定的全国第二批师带徒老中医药专家、国家药品监督管理局药品审评专家。1937年12月出生，1962年7月毕业于成都中医学院（现成都中医药大学）。中国中医药学会妇科委员会副主任委员、四川省中医药学会常务理事、四川省中医药学会妇科专委会顾问。");

        DoctorBean doctorBean2 = new DoctorBean();
        doctorBean2.setdId(2);
        doctorBean2.setDoctorName("吴克明");
        doctorBean2.setDoctorJob("住院医师");
        doctorBean2.setDoctorGood("月经不调、崩漏、痛经、闭经、卵巢早衰、多囊卵巢综合征、更年期综合征、带下病、不孕症、妇科盆腔包块及胎产痛");
        doctorBean2.setDoctorAbstrack("主任医师，教授，硕士生导师，中国中医药学会四川省妇科专委会委员，中国中西医结合学会委员。1982年毕业于成都中医学院，1986年获医学硕士学位。从事中医妇科教学、科研和临床医疗工作20余年。擅长月经不调、崩漏、痛经、闭经、卵巢早衰、多囊卵巢综合征、更年期综合征、带下病、不孕症、妇科盆腔包块及胎产痛。主持承担各级科研课题17项，获部省市级科技进步奖4项、优秀科技图书奖1项。主编、副主编和参编规划教材、教参和学术专著14部，公开发表学术论文共60余篇。");

        DoctorBean doctorBean3 = new DoctorBean();
        doctorBean3.setdId(3);
        doctorBean3.setDoctorName("要永卿");
        doctorBean3.setDoctorJob("主治医师");
        doctorBean3.setDoctorGood("盆腔炎性疾病、痛经、子宫内膜异位症、子宫腺肌病、月经不调、不孕症、妊娠相关疾病、围绝经期综合征等疾病的中医及中西医治疗");
        doctorBean3.setDoctorAbstrack("女，主治医师。成都中医药大学毕业，硕士。擅长盆腔炎性疾病、痛经、子宫内膜异位症、子宫腺肌病、月经不调、不孕症、妊娠相关疾病、围绝经期综合征等疾病的中医及中西医治疗。第二批四川省名中医工作室继承人。");

        DoctorBean doctorBean4 = new DoctorBean();
        doctorBean4.setdId(4);
        doctorBean4.setDoctorName("刘艺");
        doctorBean4.setDoctorJob("副主任医师");
        doctorBean4.setDoctorGood("不孕症、盆腔炎、子宫内膜异位症（子宫腺疾病）、月经病、绝经相关疾病 ");
        doctorBean4.setDoctorAbstrack("副主任医师。成都中医药大学毕业，硕士。擅长不孕症、盆腔炎、子宫内膜异位症（子宫腺疾病）、月经病、绝经相关疾病。全国第二批老中医药专家学术经验继承人。 ");


        DoctorBean doctorBean5 = new DoctorBean();
        doctorBean5.setdId(5);
        doctorBean5.setDoctorName("魏绍斌");
        doctorBean5.setDoctorJob("主治医师");
        doctorBean5.setDoctorGood("月经不调、痛经、子宫内膜异位症、子宫腺肌病、妇科炎症性疾病（盆腔炎、阴道炎、宫颈炎）、子宫肌瘤、围绝经期综合征（更年期综合征）、不孕症、先兆流产、习惯性流产等多种妇科常见病、多发病和疑难疾病  ");
        doctorBean5.setDoctorAbstrack("女，主任医师，教授，妇科主任，现任成都中医药大学附属医院妇临床医学院妇科教研室主任，硕士研究生导师。四川省中医学会妇科专业委员会副主任委员兼秘书，四川省医学会妇科专业委员会委员，成都市医学会妇产科分会委员，中国中医学会妇科分会常务委员。世界中医联合会妇科专业委员会常务理事。全国老中医药专家学术经验继承人，四川省学术和技术带头人后备人选，四川省有突出贡献的优秀专家。");

        docList.add(doctorBean0);
        docList.add(doctorBean1);
        docList.add(doctorBean2);
        docList.add(doctorBean3);
        docList.add(doctorBean4);
        docList.add(doctorBean5);

        return docList;
    }

    //中医科
    private static List<DoctorBean> ZhongYiKe() {
        // TODO Auto-generated method stub
        List<DoctorBean> docList = new ArrayList<DoctorBean>();
        DoctorBean doctorBean0 = new DoctorBean();
        doctorBean0.setdId(0);
        doctorBean0.setDoctorName("何光鉴");
        doctorBean0.setDoctorJob("  主任医师");
        doctorBean0.setDoctorGood("鼻炎癌、肺癌、肝癌、胰腺癌、肠癌、食道癌及风湿性关节炎、白血病、再生障碍性贫血");
        doctorBean0.setDoctorAbstrack("从事中医内科临床教学工作40余年，从事中医肿瘤临床治疗工作30多年。长于以中医辨证施治方法治疗内科肿瘤及内科疑难杂症。擅治内科肿瘤及内科疑难杂症。特别对鼻炎癌、肺癌、肝癌、胰腺癌、肠癌、食道癌及风湿性关节炎、白血病、再生障碍性贫血等的诊治有丰富的临床经验。");

        DoctorBean doctorBean1 = new DoctorBean();
        doctorBean1.setdId(1);
        doctorBean1.setDoctorName("董振华");
        doctorBean1.setDoctorJob("主任医师");
        doctorBean1.setDoctorGood("干燥综合征,类风湿,红斑狼疮,慢性肝病,中医妇科病。");
        doctorBean1.setDoctorAbstrack("男，主任医师，教授，1978年毕业于北京中医药大学， 1992年1月被人事部、卫生部、国家中医药管理局遴选为著名中医专家祝谌予教授的学术经验继承人，从事祝氏医疗经验的学习、整理和研究工作，受益良多，1995年获出师证书。2003年经过推荐和考试被国家中医药管理局确定为全国《优秀中医临床人才研修项目》培养对象之一。现为全国第五批名老中医药专家学术经验继承工作的指导老师。中医基础理论和专业知识扎实，临床经验丰富。能熟练应用中医、中西医结合技能诊治内科常见病和疑难病症。擅长于治疗风湿免疫病（干燥综合征、类风湿、红斑狼疮等）、慢性肝病、中医妇科病等。");

        DoctorBean doctorBean2 = new DoctorBean();
        doctorBean2.setdId(2);
        doctorBean2.setDoctorName("郭赛珊");
        doctorBean2.setDoctorJob("主任医师");
        doctorBean2.setDoctorGood("糖尿病及其慢性并发症、内科疑难杂病、妇科病、睡眠障碍、抑郁症、不孕不育、肿瘤术后及放化疗期间治疗");
        doctorBean2.setDoctorAbstrack("女，主任医师，教授，原中医科主任。博士生导师，1938年10月24日生于福建莆田。1960年上海第一医学院医疗系毕业。 从事中西医结合临床内科工作三十余年。 擅长糖尿病及其慢性并发症、内科疑难杂病、妇科病、睡眠障碍、抑郁症、不孕不育、肿瘤术后及放化疗期间治疗。　");

        DoctorBean doctorBean3 = new DoctorBean();
        doctorBean3.setdId(3);
        doctorBean3.setDoctorName("李忱");
        doctorBean3.setDoctorJob("主治医师");
        doctorBean3.setDoctorGood("擅长免疫病的中西医结合治疗，特别是血清阴性脊柱关节病，致力于罕见病SAPHO综合征的研究，擅长银屑病关节炎、强直性脊柱炎、掌趾脓疱病");
        doctorBean3.setDoctorAbstrack("主治医师。成都中医药大学毕业，硕士。擅长盆腔炎性疾病、痛经、子宫内膜异位症、子宫腺肌病、月经不调、不孕症、妊娠相关疾病、围绝经期综合征等疾病的中医及中西医治疗。第二批四川省名中医工作室继承人。");

        DoctorBean doctorBean4 = new DoctorBean();
        doctorBean4.setdId(4);
        doctorBean4.setDoctorName("尹德海");
        doctorBean4.setDoctorJob("副主任医师");
        doctorBean4.setDoctorGood("肾内科疾病，特别专注于糖尿病肾病的临床及基础研究。");
        doctorBean4.setDoctorAbstrack("1964年生，贵州省黄平县人。中西医结合临床医学博士，副教授。从事专业：中西医结合肾脏病、糖尿病。2011年取得卫生部主任医师职称资格。中华中医药学会补肾活血分会常委，北京中西医结合学会肾病专业会常务委员。从1986年开始一直从事临床工作至今30年，积累了较为丰富的临床经验。目前主要专注于糖尿病肾病的研究，已发表论文30余篇,《中国中西医结合杂志》、《中国中西医结合肾病杂志》等多份专业杂志审稿人。专注于糖尿病肾病的临床和基础研究，采用中西医结合的方法治疗糖尿病肾病取得较好疗效，积累了丰富的临床经验，主持国家自然科学基金糖尿病肾病系列研究课题：“从Akt-mTOR信号通路研究中西药合用治疗糖尿病肾病的机制”、“基于细胞内信号通路再平衡假说研究中药治疗糖尿病肾病的机制”等。 ");

        DoctorBean doctorBean5 = new DoctorBean();
        doctorBean5.setdId(5);
        doctorBean5.setDoctorName("张孟仁");
        doctorBean5.setDoctorJob("主治医师");
        doctorBean5.setDoctorGood("治疗糖尿病、脑血管病及内科杂病 ");
        doctorBean5.setDoctorAbstrack("男，主任医师，教授，硕士研究生导师，1963年4月出生于山西省大同市。1987年毕业于北京中医学院中医系，获医学学士学位。主任医师 、教授，硕士研究生导师。");

        docList.add(doctorBean0);
        docList.add(doctorBean1);
        docList.add(doctorBean2);
        docList.add(doctorBean3);
        docList.add(doctorBean4);
        docList.add(doctorBean5);

        return docList;
    }
    //皮肤科
    private static List<DoctorBean> PiFuKe() {
        // TODO Auto-generated method stub
        List<DoctorBean> docList = new ArrayList<DoctorBean>();
        DoctorBean doctorBean0 = new DoctorBean();
        doctorBean0.setdId(0);
        doctorBean0.setDoctorName("秦选光");
        doctorBean0.setDoctorJob("副主任医师");
        doctorBean0.setDoctorGood("1.激素依赖性皮炎、痤疮、化妆品不良反应鉴定、敏感皮肤、面部皮炎、黄褐斑； 2.扁平疣、尖锐湿疣、跖疣、寻常疣 3.皮炎、湿疹、银屑病； 4.红斑狼疮、皮肌炎、硬皮病； 5.激光美容、血管性疾病、色素性疾病、性病。");
        doctorBean0.setDoctorAbstrack("男，西安西京医院皮肤科，副主任医师，副教授。毕业于第四军医大学医疗系，1991年获硕士学位，2001年获博士学位。从事皮肤性病专业20余年，擅长面部皮肤病，色素性皮肤病，血管性皮肤病，黄褐斑，痤疮，激光美容，性病等。曾任陕西省性健康教育专业委员会常务副主任委员、陕西省性学会常务理事、中华医学会医学美学美容学青年学组委员、陕西省医学美学美容学学会委员等职务。先后被多次被评为院校级教学先进个人、先进医务工作者，曾获总后优秀教师、校十佳青年教师称号。");

        DoctorBean doctorBean1 = new DoctorBean();
        doctorBean1.setdId(1);
        doctorBean1.setDoctorName("肖茜");
        doctorBean1.setDoctorJob("副主任医师");
        doctorBean1.setDoctorGood("中医药治疗黄褐斑，脱发，痤疮，神经性皮炎，扁平疣，过敏性紫癜等常见皮肤病");
        doctorBean1.setDoctorAbstrack("女，医学博士，副主任医师。2008年毕业于第四军医大学中西医结合临床专业，获医学硕士学位，2011年毕业于第四军医大学中西医结合临床专业，获医学博士学位。自幼师从祖父学习中医，有扎实的中医理论基础和临床实践能力。擅长中医美容及中医辨证治疗皮肤病，尤其对黄褐斑、脱发、痤疮、神经性皮炎、扁平疣，过敏性紫癜、湿疹、荨麻疹等常见皮肤病有较为深入的研究，积累了丰富的临床经验。现任中华医学会陕西省药学分会会员，中华医学会皮肤性病学分会会员，陕西省中医药科技开发研究会青年分会常委，陕西省中西医结合皮肤性病学会委员，西京皮肤医院中医皮肤科主任。承担包括国家自然科学基金，陕西省科技厅社发攻关项目等多项国家及省级科研项目，在国内外发表学术论文50余篇。");

        DoctorBean doctorBean2 = new DoctorBean();
        doctorBean2.setdId(2);
        doctorBean2.setDoctorName("肖月园");
        doctorBean2.setDoctorJob("主任医师");
        doctorBean2.setDoctorGood("中西医配合治疗皮肤病，专长：荨麻疹、湿疹、痤疮、扁平疣、神经性皮炎、带状疱疹、玫瑰糠疹、激素依赖性皮炎等面部各类皮炎、过敏性紫癜、结节性红斑、血管炎及结缔组织病的中医治疗等");
        doctorBean2.setDoctorAbstrack("男，主治医师，中医外科学（皮肤性病学方向）硕士。2000年入黑龙江中医药大学中医学专业就读，2005年获中医学学士学位后攻读硕士研究生，师从全国名老中医王玉玺教授，2008年获中医外科皮肤病学硕士学位。毕业后入第四军医大学西京医院皮肤科从事住院医师工作。2010年3月—9月于北京市中医医院——暨赵炳南皮肤病研究所（赵炳南——近代中医皮肤科泰斗）进修学习，回科后从事住院总工作半年余，而后出诊至今。对中西医配合治疗荨麻疹、湿疹等过敏性皮肤病，激素依赖性皮炎等各种面部皮炎，扁平疣，痤疮，黄褐斑，银屑病，神经性皮炎，结缔组织病（如硬皮病），血管炎等皮肤病积累了丰富的临床经验。尤其擅长中医辨证治疗荨麻疹，激素依赖性皮炎、神经性皮炎等各种皮炎，湿疹，过敏性紫癜，痤疮，扁平疣，带状疱疹，黄褐斑等皮肤病。中国医师协会会员、中华中医药学会皮肤科分会青年委员，陕西省中医药科技开发研究会会员。");

        DoctorBean doctorBean3 = new DoctorBean();
        doctorBean3.setdId(3);
        doctorBean3.setDoctorName("刘斌");
        doctorBean3.setDoctorJob("主治医师");
        doctorBean3.setDoctorGood("注射微整形（全面部提升、除皱、瘦脸；面部填充微整形）毁容性皮肤病（痤疮、黄褐斑、太田痣、雀斑、晒斑、咖啡班、褐青色痣）皮肤真菌病，脱发，甲病，病毒性疣病（扁平疣、寻常疣、尖锐湿疣等）、 性传播疾病");
        doctorBean3.setDoctorAbstrack("女，主任医师，教授，现任：中华医学会陕西省医学美学与美容学分会第六届委员会副主委、中国女医师协会常委、中国医师协会美容整形医师专业委员会常委 、 中华医学会医学美学与美容学分会专业学组成员 、中西医结合会皮肤性病专业委员会真菌学组委员 ");

        DoctorBean doctorBean4 = new DoctorBean();
        doctorBean4.setdId(4);
        doctorBean4.setDoctorName("谢凤含");
        doctorBean4.setDoctorJob("副主任医师");
        doctorBean4.setDoctorGood("色素痣、表皮囊肿、脂肪瘤等常见皮损的手术切除。");
        doctorBean4.setDoctorAbstrack("14年7月就职西京医院皮肤科，任职住院医生。   ");

        DoctorBean doctorBean5 = new DoctorBean();
        doctorBean5.setdId(5);
        doctorBean5.setDoctorName("马翠玲");
        doctorBean5.setDoctorJob("主治医师");
        doctorBean5.setDoctorGood("白癜风等色素性皮肤病、儿童皮肤病、过敏性皮肤病、瘢痕、性病等诊断治疗");
        doctorBean5.setDoctorAbstrack("女，教授，主任医师，陕西省性学会健康教育专业委员会主任委员、中华医学会皮肤性病委员会性病学组委员、中国医师学会中西医结合学会痤疮专业委员会委员。1985年毕业于第一军医大学医疗专业，1985年至今在西京医院皮肤科工作，期间2000年获皮肤性病学博士学位，2001年赴美国西弗吉尼亚大学做博士后及访问工作，2007年12月回国继续在西京医院皮肤科工作。擅长白癜风等色素性皮肤病、儿童皮肤病、过敏性皮肤病、瘢痕、性病等诊断治疗");

        docList.add(doctorBean0);
        docList.add(doctorBean1);
        docList.add(doctorBean2);
        docList.add(doctorBean3);
        docList.add(doctorBean4);
        docList.add(doctorBean5);

        return docList;
    }
    //心里科
    private static List<DoctorBean> XinLiKe() {
        // TODO Auto-generated method stub
        List<DoctorBean> docList = new ArrayList<DoctorBean>();
        DoctorBean doctorBean0 = new DoctorBean();
        doctorBean0.setdId(0);
        doctorBean0.setDoctorName("焦玉梅");
        doctorBean0.setDoctorJob("副主任医师");
        doctorBean0.setDoctorGood("心境障碍（单相抑郁症、双相障碍）、焦虑障碍（包括焦虑症、恐怖症、强迫症等）、失眠、精神分裂症等常见精神障碍的诊断、治疗。抑郁症、焦虑障碍、家庭、婚姻情感、青年教育成长的咨询和心理治疗(人格障碍除外)");
        doctorBean0.setDoctorAbstrack("女，副主任医师。长期从事精神科临床工作，并对精神药物（包括抗精神病药物、抗躁狂药物、抗抑郁药物）引起的糖脂代谢问题一直进行关注和研究，并对精神分裂症后的康复、家庭成员的应付行为、情感表达等问题有一定的研究。在药物治疗的同时，更注重各类疾病的社会、心理学的相互作用。");

        DoctorBean doctorBean1 = new DoctorBean();
        doctorBean1.setdId(1);
        doctorBean1.setDoctorName("易正辉");
        doctorBean1.setDoctorJob("副主任医师");
        doctorBean1.setDoctorGood("各类抑郁症，精神分裂症，焦虑症，老年性精神病及心身疾病。");
        doctorBean1.setDoctorAbstrack("医疗专长为心境障碍及精神病性障碍诊疗技术，注重心境障碍及精神分裂症的药物治疗及心理咨询与治疗 在临床工作中积累了较丰富的有关抑郁症、躁狂症、精神分裂症、强迫症、焦虑症及失眠等诊断与治疗的临床经验。以课题负责人主持完成一项国家自然科学基金面上项目、一项上海市卫生局课题、一项上海市公共卫生人才培养课题及上海交通大学博士创新课题。以课题副组长完成一项“863”项目及一项上海申康医院发展中心科研项目。目前主持一项国家自然科学基金面上项目及上海交通大学人文社科课题项目，发表论文近130余篇，其中SCI文章40余篇。");

        DoctorBean doctorBean2 = new DoctorBean();
        doctorBean2.setdId(2);
        doctorBean2.setDoctorName("肖月园");
        doctorBean2.setDoctorJob("主任医师");
        doctorBean2.setDoctorGood("心理障碍，焦虑，恐惧，失眠，躁狂忧郁，精神分裂症的诊断；中西医结合治疗和精神康复治疗");
        doctorBean2.setDoctorAbstrack("男，主任医师。中国心理卫生协会委员、中国中医心理学研究会理事、上海市心理康复协会秘书长、上海市残疾技术鉴定委员会专家委员、中残联精神残疾康复专业委员会委员，期从事各类精神障碍的康复医疗工作，在工作实践和研究中，先后在《中国神经精神疾病杂志》、《中国康复》、《现代康复》、《英国精神疾病杂志》等国内外专业期刊上发表20余篇科研论文。曾多次参加国际学术大会发言或交流，曾多次参加获国内学术研究会“优秀论文奖”。曾获“粟宗华精神卫生科技进步二等奖” 、“山东济宁医学院科技进步二等奖” 、“上海市十佳中青年优秀医师提名奖”等。");

        DoctorBean doctorBean3 = new DoctorBean();
        doctorBean3.setdId(3);
        doctorBean3.setDoctorName("刘斌");
        doctorBean3.setDoctorJob("主治医师");
        doctorBean3.setDoctorGood("治疗失眠、紧张、焦虑、恐怖等各种情绪障碍；抑郁症、婚恋问题、人际交往问题");
        doctorBean3.setDoctorAbstrack("男，1998年毕业于上海第二医科大学（现交通大学医学院）临床医学系，现为上海市精神卫生中心副主任医生，心理治疗师。长期从事精神科临床、教学工作，目前工作于上海市精神卫生中心特色病房之一：临床心理科心身病房。在对焦虑障碍、进食障碍、心境障碍、神经症及心身疾病的药物、心理治疗方面有丰富的经验。 ");

        DoctorBean doctorBean4 = new DoctorBean();
        doctorBean4.setdId(4);
        doctorBean4.setDoctorName("谢凤含");
        doctorBean4.setDoctorJob("主治医师");
        doctorBean4.setDoctorGood("认知行为心理治疗、综合医院精神病学、抑郁和焦虑障碍的治疗、自杀预防与危机干预");
        doctorBean4.setDoctorAbstrack("男，主任医师，教授，附属中山医院心理医学科主任，硕士生导师。上海医学会行为医学专业委员会主任委员和中西医结合会心身医学分会主任委员。上海市心理卫生学会副理事长，中华医学会行为医学分会和心身医学分会常务委员，中国心理卫生协会理事暨危机干预专业委员会副主任委员。1983年毕业于上海第一医学院医学专业，1989年毕业于原上海医科大学研究生院（精神卫生专业）。自1983年以来一直在原上海医科大学、上海市精神卫生中心和上海中山医院从事临床、教学与科研，1997年晋升为教授。自1998年以来，任复旦大学医学院（原上海医科大学）医学心理学教研室主任以及上海市心理咨询中心主任医师。 ");

        DoctorBean doctorBean5 = new DoctorBean();
        doctorBean5.setdId(5);
        doctorBean5.setDoctorName("马翠玲");
        doctorBean5.setDoctorJob("主治医师");
        doctorBean5.setDoctorGood("精神分裂症，心境障碍（双相情感障碍、抑郁症、躁狂症）、神经症的治疗以及各种心理问题的咨询。");
        doctorBean5.setDoctorAbstrack("女，主任医师，擅长精神分裂症，心境障碍和神经症的治疗以及各种心理问题的咨询。从事精神病学和精神卫生专业26年，对精神科疾病的诊治有丰富的临床经验。担任临床七科主任，科室病房床位有200余只，男女患者均收住。");

        docList.add(doctorBean0);
        docList.add(doctorBean1);
        docList.add(doctorBean2);
        docList.add(doctorBean3);
        docList.add(doctorBean4);
        docList.add(doctorBean5);

        return docList;
    }
    //消化科
    private static List<DoctorBean> XiaoHuaKe() {
        // TODO Auto-generated method stub
        List<DoctorBean> docList = new ArrayList<DoctorBean>();
        DoctorBean doctorBean0 = new DoctorBean();
        doctorBean0.setdId(0);
        doctorBean0.setDoctorName("幺立萍");
        doctorBean0.setDoctorJob("副主任医师");
        doctorBean0.setDoctorGood("擅长肝硬化食管胃底静脉曲张内镜下套扎及硬化剂注射，食管良，恶性狭窄扩张及支架植入治疗，贲门失弛缓症内镜下扩张及环形肌切开（POME术），食道，胃，肠道粘膜下肿瘤内镜切除(STER术)，ESD等内镜微创手术。 消化道出血，消化道肿瘤的早期诊断及规范化、个体化治疗。擅长消化性溃疡 胃炎，肠炎的治疗");
        doctorBean0.setDoctorAbstrack("女，副主任医师，副教授，医学博士，硕士生导师，中华医学会消化学分会食管疾病协作组委员，陕西省消化学会幽门螺杆菌学组委员，陕西省整合营养学会理事，陕西省保健协会肿瘤MDT专业委员会常委，《Gut》中文版编委，以第一申请人获得国家自然科学基金资助3项，陕西省科技攻关基金1项，先后参与国家自然科学基金资助、国家教育部骨干教师资助课题研究21项。分别以第一及第三以后作者发表SCI论文共17篇，源期刊论文5篇，研究内容曾3次被国际性消化病大会DDW选为大会发言及学术交流。参编专著2部。");

        DoctorBean doctorBean1 = new DoctorBean();
        doctorBean1.setdId(1);
        doctorBean1.setDoctorName("易正辉");
        doctorBean1.setDoctorJob("副主任医师");
        doctorBean1.setDoctorGood("炎症性肠病，包括克罗恩病和溃疡性结肠炎，胃肠肿瘤。");
        doctorBean1.setDoctorAbstrack("男，主任医师，教授，博士生导师，第四军医大学西京消化病医院常务副院长。国家教育部“长江学者奖励计划”特聘教授，现任中华医学会消化病学分会常委兼副主任委员、陕西省医学会消化内科学分会主任委员，全国炎症性肠病学组核心成员。长期从事消化内科临床诊治和基础研究工作，特别是在炎症性肠病（克罗恩病和溃疡性结肠炎）诊治方面有很高造诣，在西京消化病医院，建立领导成立了西京IBD中心，包括胃肠外科，病理科，放射影像科，营养科及护理团队，设置了每周四下午炎症性肠病专病门诊，并开设IBD专病病区消化五科。");

        DoctorBean doctorBean2 = new DoctorBean();
        doctorBean2.setdId(2);
        doctorBean2.setDoctorName("韩英");
        doctorBean2.setDoctorJob("主治医师");
        doctorBean2.setDoctorGood("在黄疸的鉴别诊断、免疫性肝病的早期诊断、治疗及高危人群的筛选、不明原因的转氨酶增高及利用干细胞移植技术治疗肝硬化方面积累了丰富的经验  ");
        doctorBean2.setDoctorAbstrack("女，主任医师，教授，消化九科主任，非传染肝病科主任，1965年8月出生。医学博士，曾留日本东京大学。任第四军医大学第一附属医院消化内科主任医师, 教授, 博士生导师，精品教员消化病医院消化九科主任。");

        DoctorBean doctorBean3 = new DoctorBean();
        doctorBean3.setdId(3);
        doctorBean3.setDoctorName("刘斌");
        doctorBean3.setDoctorJob("主治医师");
        doctorBean3.setDoctorGood("肝、胆、胰肿瘤的综合介入治疗；良恶性梗阻性黄疸的胆道引流及支架植入；良恶性下腔静梗阻的球囊扩张及支架植入；肝硬化消化道出血经颈静门腔分流术（TIPS）治疗；脾功能亢进的脾动脉栓塞治疗；下肢静脉血栓形成后下腔静脉过滤器植入预防肺动脉梗塞    ");
        doctorBean3.setDoctorAbstrack("医学博士，主任医师、教授，博导，消化介入科主任。中华医学会放射学分会介入学组副组长、中华医学会消化分会介入协作组副组长、中国抗癌学会陕西省肿瘤介入分会副主任委员、陕西省医学会介入放射学分会副主任委员、 陕西省肿瘤综合治疗委员会副主任委员、卫生部肿瘤专业委员会和综合介入专业委员会特聘专家，曾在日本金泽大学和香港大学留学。主要研究方向：一、门静脉血栓和/或海绵样变性的介入治疗。二、TIPS治疗肝硬化门脉高压静脉曲张出血和顽固性腹水。三、布加氏综合征的介入治疗。四、肝癌的介入治疗和分子靶向治疗。");

        DoctorBean doctorBean4 = new DoctorBean();
        doctorBean4.setdId(4);
        doctorBean4.setDoctorName("韩国宏");
        doctorBean4.setDoctorJob("主治医师");
        doctorBean4.setDoctorGood("消化系统常见病、多发病及危急重症的诊断治疗方面有丰富的经验。特别是在急慢性肝病的诊治");
        doctorBean4.setDoctorAbstrack("男，主任医师，教授，消化八科（肝病科）主任，1963年9月生。医学博士。学术任职：中华医学会消化分会肝胆疾病协作组委员；中华医学会感染病分会肝衰竭和人工肝学组委员；陕西省肝病学会副主任委员。");

        DoctorBean doctorBean5 = new DoctorBean();
        doctorBean5.setdId(5);
        doctorBean5.setDoctorName("谢华红");
        doctorBean5.setDoctorJob("主治医师");
        doctorBean5.setDoctorGood("1、食管静脉曲张套扎硬化、胃底静脉曲张组织胶注射 2、消化道良恶性狭窄的镜下扩张及支架植入 3、贲门失弛缓症POEM术 4、消化道早癌及粘膜下病变的ESD、STER、ESE、EFR等");
        doctorBean5.setDoctorAbstrack("男，主治医师，讲师，擅长常见消化系统疾病的诊治。 ");

        docList.add(doctorBean0);
        docList.add(doctorBean1);
        docList.add(doctorBean2);
        docList.add(doctorBean3);
        docList.add(doctorBean4);
        docList.add(doctorBean5);

        return docList;
    }
    //普外科
    private static List<DoctorBean> PuWaiKe() {
        // TODO Auto-generated method stub
        List<DoctorBean> docList = new ArrayList<DoctorBean>();
        DoctorBean doctorBean0 = new DoctorBean();
        doctorBean0.setdId(0);
        doctorBean0.setDoctorName("费健");
        doctorBean0.setDoctorJob("副主任医师");
        doctorBean0.setDoctorGood("胆道、胰腺复杂疾病的诊治、甲状腺疾病、外科体表肿瘤等的诊治");
        doctorBean0.setDoctorAbstrack("男，主任医师，擅长胆道胰腺外科疾病的诊治，师从我国著名胆道、胰腺外科专家张圣道教授，2001年上海第二医科大学博士毕业，2005-2006年赴欧洲胰腺外科中心——德国Ulm大学外科医院学习，擅长胆道、胰腺复杂疾病的诊治，如肝内外胆管结石、胆道恶性肿瘤、重症急性胰腺炎、胰腺癌等。此外，对甲状腺疾病、外科体表肿瘤等的诊治等也颇为擅长");

        DoctorBean doctorBean1 = new DoctorBean();
        doctorBean1.setdId(1);
        doctorBean1.setDoctorName("李勤裕");
        doctorBean1.setDoctorJob("主任医师");
        doctorBean1.setDoctorGood("甲状腺外科、肝胆消化道肿瘤外科及机器人、腔镜微创外科技术 （肝胆外科手术、肠癌肝转移手术和多学科团队综合治疗、甲状腺、甲状旁腺良恶性肿瘤手术及腔镜微创技术、射频消融技术应用）");
        doctorBean1.setDoctorAbstrack("，副教授、副主任医师、医学博士。兼任中国研究型医院学会甲状腺疾病专业委员会甲状腺手术学组委员、中国医促会结直肠癌肝转移治疗委员会委员、中国研究型医院学会肿瘤外科专业委员会委员、上海市肿瘤防治联盟委员。");

        DoctorBean doctorBean2 = new DoctorBean();
        doctorBean2.setdId(2);
        doctorBean2.setDoctorName("严佶祺");
        doctorBean2.setDoctorJob("主治医师");
        doctorBean2.setDoctorGood("各类复杂良恶性病变的甲状腺手术，包括功能性颈淋巴结清扫术，胸骨后甲状腺肿切除，甲亢甲状腺切除，达芬奇机器人技术在甲状腺微创手术中的应用；各类门脉高压手术；肝胆胰手术和消化道肿瘤手术。 ");
        doctorBean2.setDoctorAbstrack("男，主任医师、副教授，硕士生导师。目前担任瑞金医院普外科甲状腺血管外科病区主任；中国医生协会外科医师分会机器人外科医师委员会委员；中国医师协会外科医师分会甲状腺外科医师委员会（CTA） 中青年委员；中国研究型医院学会甲状腺疾病专业委员会甲状旁腺保护学组委员；中国研究型医院学会甲状腺疾病专业委员会甲状腺手术学组委员；中国研究型医院学会甲状腺疾病专业委员会智能机器人学组副组长；中国研究型医院学会甲状腺疾病专业委员会腔镜手术学组委员。");

        DoctorBean doctorBean3 = new DoctorBean();
        doctorBean3.setdId(3);
        doctorBean3.setDoctorName("刘斌");
        doctorBean3.setDoctorJob("主治医师");
        doctorBean3.setDoctorGood("胃肠道肿瘤，胆道、甲状腺疾病的微创治疗");
        doctorBean3.setDoctorAbstrack("男，主任医师，普外科副主任，医学博士，硕士生导师。１９８９年７月毕业于上海第二医科大学医疗系；１９９３年９月－１９９６年６月在上海第二医科大学攻读硕士学位，获得外科学（胆道）医学硕士学位。２００３年赴美国加州Ｆｒｅｓｎｏ医院进修学习。现担任上海市微创外科临床医学中心副主任、中华医学会上海外科分会甲状腺学组委员，亚洲内镜外科协会ＥＬＳＡ会员，多本医学专业杂志的编委。");

        DoctorBean doctorBean4 = new DoctorBean();
        doctorBean4.setdId(4);
        doctorBean4.setDoctorName("狄忠民");
        doctorBean4.setDoctorJob("主治医师");
        doctorBean4.setDoctorGood("门静脉高压，甲状腺肿瘤，消化道肿瘤的诊治");
        doctorBean4.setDoctorAbstrack("男，副主任医师，副教授，上海市抗癌协会胃肠肿瘤专业委员会会员。1990年毕业于上海第二医科大学临床医学系，1997年获得外科学硕士学位。瑞金医院外科工作16年，是一名在医、教、研各方面发挥出色的多面手。");

        DoctorBean doctorBean5 = new DoctorBean();
        doctorBean5.setdId(5);
        doctorBean5.setDoctorName("谢华红");
        doctorBean5.setDoctorJob("主治医师");
        doctorBean5.setDoctorGood("胆道疾病的微创治疗，包括腹腔镜胆囊及胆管手术，内镜ERCP（逆行胆胰管造影）操作。胃肠肿瘤的腹腔镜手术。");
        doctorBean5.setDoctorAbstrack("男，副主任医师，硕士研究生，1993年毕业于上海第二医科大学，同年进入瑞金医院普外科工作至今。现任上海交通大学医学院附属瑞金医院普外科副主任医师，上海市微创外科临床医学中心副主任医师。");

        docList.add(doctorBean0);
        docList.add(doctorBean1);
        docList.add(doctorBean2);
        docList.add(doctorBean3);
        docList.add(doctorBean4);
        docList.add(doctorBean5);

        return docList;
    }



}
