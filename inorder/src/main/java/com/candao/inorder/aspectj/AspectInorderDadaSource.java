package com.candao.inorder.aspectj;

import org.aspectj.lang.JoinPoint;
public class AspectInorderDadaSource {

		public void beforeDaoMethod(JoinPoint joinPoint){
			System.out.println(joinPoint+"pppppppppppppppppppppppppppppppppppppppppppppppppppp");
		}
		
}
