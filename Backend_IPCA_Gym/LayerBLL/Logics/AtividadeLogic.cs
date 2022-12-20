﻿using LayerBLL.Utils;
using LayerBOL.Models;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;
using LayerDAL.Services;

namespace LayerBLL.Logics
{
    public class AtividadeLogic
    {
        public static async Task<Response> GetAtividadesLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Atividade> atividadeList = await AtividadeService.GetAllService(sqlDataSource);

            if (atividadeList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult(atividadeList);
            }

            return response;
        }
    }
}
