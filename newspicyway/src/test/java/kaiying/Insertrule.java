package kaiying;

public class Insertrule {

	public static void main(String [] args){
		
		String [] arg = string.split(",");
		for(int i = 0;i< arg.length ;i++){
			String sql = "INSERT INTO t_parterner_coupon (`id`, `couponid`, `parternerid`, `dishid`, `discountamount`, "
					+ "`discountrate`) \n" +
					"		VALUES ("+(i+1)+", '6', '52', '"+arg[i]+"', NULL, '90.00');";
			
			System.out.println(sql);
		}

	}
	
	public static String string = 
			"017aebe9-21b0-47de-93af-04bcd2907876,\n" +
					"01d3d988-06a4-4764-bfd5-0702bd426817,\n" +
					"0264eeb2-dd8d-4e45-b184-e532eb9adfa6,\n" +
					"036d418c-8e90-40c3-b083-c3f8e783ce8b,\n" +
					"036f9a35-2882-4114-a31b-dc9c278718a6,\n" +
					"094fd831-e57d-4060-a646-93cea44eca01,\n" +
					"09c723e7-7cd3-43c5-ae9d-98e41e86e75f,\n" +
					"0af80d0e-511e-4d42-89a3-b4882f9dae3b,\n" +
					"11e7b2fd-8dc8-4e56-acc0-83c1acda9d7a,\n" +
					"1308fbb9-cf5c-4d72-9b7a-b3d6db139435,\n" +
					"13ba2c98-6ca0-4f09-ab3a-20b1e03570df,\n" +
					"13db9e0e-01a9-49ac-a75d-758b4bca935e,\n" +
					"142ea19e-9e21-4cac-a3c7-5dd70d8397f3,\n" +
					"15c75c91-c1e7-40c8-a6ac-01345d147d8f,\n" +
					"15d7fa0e-6855-43c5-8eab-c18f4888ecb1,\n" +
					"15fd50ba-feca-4c2e-a72c-c429395882f8,\n" +
					"16a50aeb-d54b-4a07-831a-a874b7a5081c,\n" +
					"17ce38e6-b342-416a-9e57-07927565b0cd,\n" +
					"19c57794-835e-49f0-b834-ad699111176f,\n" +
					"1ac0d050-a506-4159-9973-7a04d39367c5,\n" +
					"1b1c763c-3f49-4c93-9714-489eafdc5675,\n" +
					"1b5bb1a3-1dbd-4a91-bc1b-a0bdf14dae85,\n" +
					"21962b8a-99b7-433d-8425-76ec43a1a7d5,\n" +
					"231aec1e-655b-41c6-bafe-74cfef27a2fd,\n" +
					"26ecea88-d65d-4af9-8550-afa2d7155281,\n" +
					"271168b8-988d-4a48-9e47-ab54cd6512dd,\n" +
					"27290462-487a-4b7a-92ce-2f66728f136b,\n" +
					"2877608e-d0df-4ade-9550-6e9bdab75499,\n" +
					"2a8011b8-1bf1-4ad1-93d0-0c613849e70c,\n" +
					"2f4697fa-d315-48f0-aaff-8127273e54cb,\n" +
					"2fa0733e-2df7-4653-bd52-01b0799574bc,\n" +
					"303544fd-5707-4394-b72e-25d48799e03a,\n" +
					"3216fb85-0055-4b56-ad8b-66fcd60a9eac,\n" +
					"32ffbe8a-97be-4cf0-b88f-9edefad2caf0,\n" +
					"340c9c72-c77f-47d9-ada4-a95987840e13,\n" +
					"34ca58a6-a10c-42f9-bf49-381ce4f04bc8,\n" +
					"35a04aca-337e-451b-af35-74e4f208969e,\n" +
					"36cc7095-309f-469e-88d2-cf8aab02dfae,\n" +
					"36ddd06c-b192-4cf8-8de4-3164e9e91519,\n" +
					"37a9b822-b3bd-4a7a-8ea8-785451d314e4,\n" +
					"39a31cbf-8c26-4b32-8732-52b0eec8296b,\n" +
					"3d145768-e0f0-487c-bf4e-33b6f3f5d758,\n" +
					"3d3cee34-d939-48ea-97d1-975dff43c418,\n" +
					"3d61d592-bbf9-4ece-a046-24b607b01da8,\n" +
					"3ea1da6b-e43d-4aaf-b7f1-e6382b8feb32,\n" +
					"3ea40079-b36f-4695-964f-0633e93bc196,\n" +
					"42e16d5d-11fa-4b1c-83ca-6089dc56e4dc,\n" +
					"43e76f55-e6e3-471b-8252-401f4766dbd9,\n" +
					"43f5fcab-2a9f-442d-b202-30b84fd330ff,\n" +
					"4422065b-d5b2-4556-b483-88509201277b,\n" +
					"47d12347-0085-429f-aa7d-9fd721bfa006,\n" +
					"4b7f62c2-a709-4b4b-9b73-ac20c97ca701,\n" +
					"4c253002-1165-45b2-9bfc-1499b1d36aaa,\n" +
					"4c716b25-6f4f-4105-a1e6-056fcdff3ac5,\n" +
					"4e1ac892-7449-4dd3-9ae1-2595b046541b,\n" +
					"51aa380d-5b72-4468-8274-f7bfadb1abe1,\n" +
					"51b7ef13-ee6c-4669-a53e-31a605c7c99d,\n" +
					"51d050f4-c9f6-4dbf-9ce1-0d3b83df88e6,\n" +
					"52302c1a-126b-4982-a631-03ebf0323d02,\n" +
					"58063585-93f5-4b00-97ad-339a7303a0af,\n" +
					"585fd2b1-6834-4b05-8227-3e995af7dd44,\n" +
					"591dedc2-831a-49d3-aa64-72f82454c463,\n" +
					"5b7470a0-7b50-4463-9c04-97621513d1e1,\n" +
					"609c6fb4-b1f9-4092-be04-e42237c3f37f,\n" +
					"615cb03f-1192-4705-8a40-f7bf8ce6cd03,\n" +
					"63cd9f69-5aab-4abc-8855-36ff896e8ae8,\n" +
					"6516d245-2c34-4b39-8c5b-0c9d78fd2c7c,\n" +
					"65fd5425-aacf-411e-a894-dcebcc9f0079,\n" +
					"680a661a-9ad9-48ef-b914-c6c4c8459998,\n" +
					"69098029-2cf3-4e82-b23b-90b7e6e19bf8,\n" +
					"6d3877b5-d8a4-4f54-ba75-d1fd4a832a04,\n" +
					"6e4d7681-c057-4d23-adaf-553307132505,\n" +
					"6f1b3803-4fbd-4620-97e5-f0f511859875,\n" +
					"70500694-ca74-4bce-aaa3-7dfa2ad37126,\n" +
					"721c27b5-e325-4430-9ba6-ebd1f6dc1d93,\n" +
					"72a630fa-5df4-49ed-a45a-1f3304534144,\n" +
					"73d0f781-4a65-432b-a290-90c7eb45fbaf,\n" +
					"7446ce7a-ba69-49eb-b29f-cd06c43a87ae,\n" +
					"74ec4b2c-c381-4dc6-bf64-5fad6eef1929,\n" +
					"7569dea4-c84f-4d8b-8b00-6e591fc46778,\n" +
					"75f52fac-efd8-44b5-a038-c2a781bc2966,\n" +
					"773f7fa5-3949-4895-9ecd-6dbcdfa22af8,\n" +
					"7aef94bc-f214-4901-8102-19e6edb70a69,\n" +
					"7b8c085a-80cc-4882-8cc5-a918df926a4d,\n" +
					"7d912ab9-2b74-4c8c-aa24-08b4c1ea4655,\n" +
					"7f66aa2a-56a1-4003-9ee5-ce45823c6b96,\n" +
					"82d0a92c-a199-4644-8e66-cbf2b9901243,\n" +
					"8641a2de-0f5f-43d6-8a69-91a8b71cc143,\n" +
					"8797d905-a4f8-4fd0-963f-a4f129f46c26,\n" +
					"87f8e159-ab43-4c21-8227-6585c357bd3d,\n" +
					"8a2773fb-d6d0-4638-9f58-d595d1e50b5c,\n" +
					"8abbcdcd-d41a-49f7-b704-3eafcfc37253,\n" +
					"8ae66a84-f128-4bd7-aa8d-aaf9e2f2dfa6,\n" +
					"8c98a694-97f4-4ca4-9d12-7067a6bc1c87,\n" +
					"8ef64206-7916-49c0-8ec3-31e9f27db537,\n" +
					"902b9dd7-345f-4322-97b9-fae440e70229,\n" +
					"910fc2df-6826-43c6-8ada-57976b398892,\n" +
					"919c247f-d050-4ab8-a588-91b824bbee65,\n" +
					"927f1b79-5909-4033-9246-7d52a919b744,\n" +
					"950f54d7-9e4f-498f-9cfc-72b9388435d0,\n" +
					"958552ef-559e-4751-a755-f1d69309578d,\n" +
					"95860f54-53cb-425d-a99d-f173632c3172,\n" +
					"994a2ea0-9521-4f26-8936-cfa6ed50878d,\n" +
					"9ffe903f-7252-4afd-90ae-f4f6f3e6dad4,\n" +
					"a04c88df-63c3-4eb9-8e6a-67ac999fccb9,\n" +
					"a050e452-3db3-4bcc-882a-492236aaaaba,\n" +
					"a22b37b1-b7cd-4ef3-b143-5ea8ed877bb6,\n" +
					"a2b07658-35b1-4133-9c91-2e96055d9767,\n" +
					"a2b5d8e4-5cf8-4b2c-9f17-85b299d61834,\n" +
					"a3b1e93e-54ea-4065-a7bf-8745eeadfc1e,\n" +
					"a3c21303-a5a0-4c14-afa3-f5fc8896920b,\n" +
					"a45856d0-dde9-40a8-98bd-0acfc4f40a3d,\n" +
					"a5e43602-9882-42ad-8de5-a69b7911c080,\n" +
					"a70e68b1-399b-40f8-90e3-cac066a30010,\n" +
					"a807f5e0-1324-49bf-9083-a5cd91bfba0b,\n" +
					"a86f1ff2-68cd-4ca8-b8ab-23d3a7e3eee2,\n" +
					"a91caaf3-c0d3-41d6-9dc2-e499ced30536,\n" +
					"ad54dc26-e74a-4fa3-83fd-b3e2f1e2113c,\n" +
					"b1234c62-19b3-4778-87e0-52210209228f,\n" +
					"b26b2505-b9be-4f8b-af25-7e2f891585df,\n" +
					"b651cf96-cb98-4a95-82ef-f5645f122ce1,\n" +
					"b69afca7-2695-410d-9264-b63f101a2795,\n" +
					"b8b6b81f-daa7-45c7-803c-d469ce05ebfc,\n" +
					"ba98ec7f-8b6c-43dc-a3cc-704810c17ba4,\n" +
					"bc17d4d2-4858-4bf0-aa6b-b53f006a7ad6,\n" +
					"bc7291cf-3f7a-4fb6-8d52-b67a03f3877c,\n" +
					"bd933638-584d-4a8f-a548-8f1b68ede846,\n" +
					"bdf6678c-46c9-407d-b14a-567037c0b65f,\n" +
					"bf464b1d-d86b-48e7-947c-30d62da724db,\n" +
					"c0ce1b71-eae6-4273-b005-f59f533310ee,\n" +
					"c1856b82-f0cb-4ac0-9a37-d9a9772f2c2d,\n" +
					"c1929b38-978c-4019-bf63-540a97f5ffc2,\n" +
					"c1b4baea-687e-495a-8a4b-3e9d2a6c936b,\n" +
					"c3a57394-cd4a-45f7-8bbb-425e9dca31b8,\n" +
					"c605141a-1af4-4a4a-ae6c-51d5bd0c28c4,\n" +
					"c61b90ba-c79c-4318-9d52-bf2178888076,\n" +
					"cbd57a02-006a-4186-90b1-8bb072ef7adc,\n" +
					"d01d6d74-8d13-43c7-8e1b-3fa97c1670ef,\n" +
					"d52a2da1-b0af-42d6-a0db-777644bb7dea,\n" +
					"d815ac17-a5ad-4563-9107-1710c8294079,\n" +
					"d8196d61-b306-405d-a49a-4eb471229049,\n" +
					"d83c274e-82eb-4c27-9bf9-cd057a4f1369,\n" +
					"da2cd23f-ef6a-4a78-83d5-546615714982,\n" +
					"dabc1a71-4df1-401c-856e-7d9a78a03b7b,\n" +
					"dd8ec00b-afb5-4106-ade9-b5175104a13c,\n" +
					"dde283a9-fa38-4f28-9463-0ba64489db4c,\n" +
					"e0bfcc6b-1572-4888-835c-e628bea2c291,\n" +
					"e0ea3f9d-7467-4b95-8616-71edef6bf7be,\n" +
					"e1207556-eade-4ec6-b85d-5c3f3bdacc6c,\n" +
					"e345ee28-a559-4f09-b0b2-aa2306a0fc6b,\n" +
					"e439af51-264b-4c01-baca-6e7fe71da40f,\n" +
					"e560ecca-59a8-447f-8c66-4b0f9ea5848b,\n" +
					"e56858cf-76d5-4ecb-9970-870b74439103,\n" +
					"e612116a-b800-4ef9-9e3e-5417982328da,\n" +
					"e8b924ad-e514-4edb-bade-27e3777f7884,\n" +
					"e8f86fe0-5e51-4335-b9a6-1f64bd200dcc,\n" +
					"e9f0f3b6-255c-40dd-8867-c7ea98a75882,\n" +
					"eaa38aa0-1bd2-448f-a130-f9ab9999265f,\n" +
					"ebbb2173-75d6-4abe-b2b6-fe0476511cd9,\n" +
					"f0445d47-03c1-4d66-b2a8-3785b332aadf,\n" +
					"f0cdfeb4-36db-4b7d-8d2a-cc13e48f67dd,\n" +
					"f0e04872-1d70-4677-9b84-0c71321acaa2,\n" +
					"f1c4131f-8f36-41c1-bca3-6fb85561a5a4,\n" +
					"f27a4a75-a22f-46bd-9429-8f57701dba63,\n" +
					"f47ba3bd-d41c-42a2-9f7c-1081f49402b8,\n" +
					"f515986f-8117-4a18-8480-0aa97f287788,\n" +
					"f76b77a5-9de1-45df-ab23-a8ce9dbfa6d0,\n" +
					"fb285e92-66a1-4ab2-b4a9-0f8e16bb8155,\n" +
					"fb2c9b72-540d-4d6c-a633-c715704a52a4,\n" +
					"fbfda38a-b4c9-489d-86f0-2a3933db6521,\n" +
					"fdb49530-3080-4feb-b4a4-e75bb0de0b15,\n" +
					"ff08122f-5056-4af0-ae3c-78dc5861932f,\n" +
					"ff395ac1-1407-485c-bc3c-646761306419,";
}
